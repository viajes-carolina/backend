package com.viajescarolina.api.blog.infrastructure.persistence;

import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheBlogCategoryRepository implements BlogCategoryRepository, PanacheRepositoryBase<BlogCategoryPanacheEntity, Long> {

    @Override
    public List<BlogCategory> findAllActive() {
        List<BlogCategoryPanacheEntity> entities = list("active = true order by displayOrder asc, id asc");
        return entities.stream().map(this::toDomain).toList();
    }

    @Override
    public List<BlogCategory> findAllAdmin() {
        List<BlogCategoryPanacheEntity> entities = list("order by displayOrder asc, id asc");
        return entities.stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<BlogCategory> findCategoryById(Long id) {
        return find("id", id).firstResultOptional().map(this::toDomain);
    }

    @Override
    public Optional<BlogCategory> findBySlug(String slug) {
        return find("slug", slug).firstResultOptional().map(this::toDomain);
    }

    @Override
    public BlogCategory save(BlogCategory domain) {
        BlogCategoryPanacheEntity entity;
        if (domain.getId() != null) {
            entity = findById(domain.getId());
            if (entity == null) entity = new BlogCategoryPanacheEntity();
        } else {
            entity = new BlogCategoryPanacheEntity();
        }

        entity.name = domain.getName();
        entity.slug = domain.getSlug();
        entity.description = domain.getDescription();
        entity.displayOrder = domain.getDisplayOrder() != null ? domain.getDisplayOrder() : 1;
        entity.active = domain.getActive() != null ? domain.getActive() : true;
        entity.updatedAt = Instant.now();

        if (entity.id == null) {
            persist(entity);
        }

        return toDomain(entity);
    }

    @Override
    public void delete(Long id) {
        BlogCategoryPanacheEntity entity = findById(id);
        if (entity != null) {
            entity.active = false;
            entity.updatedAt = Instant.now();
        }
    }

    private BlogCategory toDomain(BlogCategoryPanacheEntity e) {
        return new BlogCategory(
                e.id,
                e.name,
                e.slug,
                e.description,
                e.displayOrder,
                e.active,
                e.createdAt,
                e.updatedAt
        );
    }
}
