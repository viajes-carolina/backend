package com.viajescarolina.api.blog.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheBlogPostRepository implements BlogPostRepository, PanacheRepositoryBase<BlogPostPanacheEntity, Long> {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public List<BlogPost> findPublicPosts(String categorySlug, String search, Boolean isFeatured, int page, int size) {
        // "from ... left join fetch category where ..." en vez del atajo implícito
        // "where ..." de Panache: necesitamos el JOIN FETCH explícito para traer la
        // categoría en la misma query y evitar un lazy-load por cada categoría distinta.
        // Alias "p" explícito: sin él, "active"/"id" quedan ambiguos entre
        // BlogPostPanacheEntity y la categoría recién unida (ambas tienen esas columnas).
        StringBuilder hql = new StringBuilder("from BlogPostPanacheEntity p left join fetch p.category where p.active = true and p.status = 'PUBLISHED'");
        Parameters params = new Parameters();

        if (categorySlug != null && !categorySlug.isBlank()) {
            hql.append(" and p.category.slug = :categorySlug");
            params.and("categorySlug", categorySlug.trim());
        }

        if (isFeatured != null) {
            hql.append(" and p.isFeatured = :isFeatured");
            params.and("isFeatured", isFeatured);
        }

        if (search != null && !search.isBlank()) {
            // ILIKE (no lower(...)+LIKE) para poder usar los índices GIN
            // idx_blog_post_title_trgm / idx_blog_post_summary_trgm (gin_trgm_ops), que están
            // sobre la columna cruda. lower(title) LIKE forzaba un Seq Scan (648ms vs 4.7ms
            // con el índice) porque Postgres no puede usar un índice sobre `title` para
            // resolver una expresión `lower(title)`.
            hql.append(" and (p.title ILIKE :search or p.summary ILIKE :search)");
            params.and("search", "%" + search.trim() + "%");
        }

        hql.append(" order by p.publishedAt desc, p.id desc");

        PanacheQuery<BlogPostPanacheEntity> query = find(hql.toString(), params);
        if (size > 0) {
            query = query.page(page, size);
        }
        return query.list().stream().map(this::toDomain).toList();
    }

    @Override
    public long countPublicPosts(String categorySlug, String search, Boolean isFeatured) {
        StringBuilder hql = new StringBuilder("active = true and status = 'PUBLISHED'");
        Parameters params = new Parameters();

        if (categorySlug != null && !categorySlug.isBlank()) {
            hql.append(" and category.slug = :categorySlug");
            params.and("categorySlug", categorySlug.trim());
        }

        if (isFeatured != null) {
            hql.append(" and isFeatured = :isFeatured");
            params.and("isFeatured", isFeatured);
        }

        if (search != null && !search.isBlank()) {
            // Mismo motivo que en findPublicPosts: ILIKE sí usa el índice GIN trigram.
            hql.append(" and (title ILIKE :search or summary ILIKE :search)");
            params.and("search", "%" + search.trim() + "%");
        }

        return count(hql.toString(), params);
    }

    @Override
    public Optional<BlogPost> findPublicBySlug(String slug) {
        return find("slug = ?1 and active = true and status = 'PUBLISHED'", slug)
                .firstResultOptional()
                .map(this::toDomain);
    }

    @Override
    public List<BlogPost> findRelatedPosts(Long categoryId, Long excludePostId, int limit) {
        return find("from BlogPostPanacheEntity p left join fetch p.category where p.category.id = ?1 and p.id != ?2 and p.active = true and p.status = 'PUBLISHED' order by p.publishedAt desc",
                categoryId, excludePostId)
                .page(0, limit)
                .list()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<BlogPost> findAllAdmin(String status, String search, int page, int size) {
        // Mismo motivo que findPublicPosts: JOIN FETCH explícito para traer la
        // categoría en la misma query y evitar un lazy-load por categoría distinta.
        // Alias "p" explícito: sin él, "id" queda ambiguo entre BlogPostPanacheEntity
        // y la categoría recién unida (ambas tienen columna "id").
        StringBuilder hql = new StringBuilder("from BlogPostPanacheEntity p left join fetch p.category where 1 = 1");
        Parameters params = new Parameters();

        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            hql.append(" and p.status = :status");
            params.and("status", status.trim().toUpperCase());
        }

        if (search != null && !search.isBlank()) {
            // ILIKE sobre title para usar idx_blog_post_title_trgm; slug no tiene índice
            // trigram (búsqueda admin por slug es de bajo volumen), se mantiene con LIKE.
            hql.append(" and (p.title ILIKE :search or lower(p.slug) like :search)");
            params.and("search", "%" + search.trim().toLowerCase() + "%");
        }

        hql.append(" order by p.id desc");

        PanacheQuery<BlogPostPanacheEntity> query = find(hql.toString(), params);
        if (size > 0) {
            query = query.page(page, size);
        }
        return query.list().stream().map(this::toDomain).toList();
    }

    @Override
    public long countAllAdmin(String status, String search) {
        StringBuilder hql = new StringBuilder("1 = 1");
        Parameters params = new Parameters();

        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            hql.append(" and status = :status");
            params.and("status", status.trim().toUpperCase());
        }

        if (search != null && !search.isBlank()) {
            hql.append(" and (title ILIKE :search or lower(slug) like :search)");
            params.and("search", "%" + search.trim().toLowerCase() + "%");
        }

        return count(hql.toString(), params);
    }

    @Override
    public Optional<BlogPost> findPostById(Long id) {
        return find("id", id).firstResultOptional().map(this::toDomain);
    }

    @Override
    public BlogPost save(BlogPost domain) {
        BlogPostPanacheEntity entity;
        if (domain.getId() != null) {
            entity = findById(domain.getId());
            if (entity == null) entity = new BlogPostPanacheEntity();
        } else {
            entity = new BlogPostPanacheEntity();
        }

        entity.slug = domain.getSlug();
        entity.title = domain.getTitle();
        entity.summary = domain.getSummary();
        entity.contentMarkdown = domain.getContentMarkdown();

        if (domain.getCategoryId() != null) {
            entity.category = BlogCategoryPanacheEntity.findById(domain.getCategoryId());
        }

        entity.coverMediaId = domain.getCoverMediaId();
        entity.coverFocalX = domain.getCoverFocalX() != null ? domain.getCoverFocalX() : 50.0;
        entity.coverFocalY = domain.getCoverFocalY() != null ? domain.getCoverFocalY() : 50.0;
        entity.authorAdvisorId = domain.getAuthorAdvisorId();
        entity.readingTimeMinutes = domain.getReadingTimeMinutes() != null ? domain.getReadingTimeMinutes() : 5;

        try {
            entity.tagsJson = objectMapper.writeValueAsString(domain.getTags() != null ? domain.getTags() : List.of());
        } catch (Exception e) {
            entity.tagsJson = "[]";
        }

        entity.status = domain.getStatus() != null ? domain.getStatus() : "PUBLISHED";
        entity.publishedAt = domain.getPublishedAt();
        entity.viewCount = domain.getViewCount() != null ? domain.getViewCount() : 0L;
        entity.isFeatured = domain.getIsFeatured() != null ? domain.getIsFeatured() : false;
        entity.active = domain.getActive() != null ? domain.getActive() : true;
        entity.updatedAt = Instant.now();

        if (entity.id == null) {
            persist(entity);
        }

        return toDomain(entity);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        update("viewCount = viewCount + 1, updatedAt = ?1 where id = ?2", Instant.now(), id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BlogPostPanacheEntity entity = findById(id);
        if (entity != null) {
            entity.active = false;
            entity.status = "ARCHIVED";
            entity.updatedAt = Instant.now();
        }
    }

    @Override
    public long countByAuthorAdvisorId(Long advisorId) {
        return count("authorAdvisorId", advisorId);
    }

    private BlogPost toDomain(BlogPostPanacheEntity e) {
        List<String> tags = new ArrayList<>();
        if (e.tagsJson != null && !e.tagsJson.isBlank()) {
            try {
                tags = objectMapper.readValue(e.tagsJson, new TypeReference<List<String>>() {});
            } catch (Exception ignored) {
            }
        }

        String catName = e.category != null ? e.category.name : "";
        String catSlug = e.category != null ? e.category.slug : "";

        return new BlogPost(
                e.id,
                e.slug,
                e.title,
                e.summary,
                e.contentMarkdown,
                e.category != null ? e.category.id : null,
                catName,
                catSlug,
                e.coverMediaId,
                e.coverFocalX,
                e.coverFocalY,
                e.authorAdvisorId,
                e.readingTimeMinutes,
                tags,
                e.status,
                e.publishedAt,
                e.viewCount,
                e.isFeatured,
                e.active,
                e.createdAt,
                e.updatedAt
        );
    }
}
