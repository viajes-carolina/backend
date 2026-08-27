package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.CreateOrUpdateBlogPostRequest;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.common.audit.Audited;
import com.viajescarolina.api.about.domain.TravelAdvisor;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CreateBlogPostUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    BlogCategoryRepository categoryRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TravelAdvisorRepository travelAdvisorRepository;

    @Audited(action = "CREATE_BLOG_POST", entityType = "BLOG_POST")
    @Transactional
    public BlogPostDTO execute(CreateOrUpdateBlogPostRequest req) {
        BlogCategory category = categoryRepository.findCategoryById(req.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + req.categoryId()));

        BlogPost post = new BlogPost();
        post.setSlug(req.slug().trim().toLowerCase());
        post.setTitle(req.title().trim());
        post.setSummary(req.summary().trim());
        post.setContentMarkdown(req.contentMarkdown());
        post.setCategoryId(category.getId());
        post.setCategoryName(category.getName());
        post.setCategorySlug(category.getSlug());

        Long coverMediaId = req.coverMediaId() != null && mediaRepository.findMediaById(req.coverMediaId()).isPresent()
                ? req.coverMediaId() : null;
        post.setCoverMediaId(coverMediaId);
        post.setCoverFocalX(req.coverFocalX() != null ? req.coverFocalX() : 50.0);
        post.setCoverFocalY(req.coverFocalY() != null ? req.coverFocalY() : 50.0);

        travelAdvisorRepository.findAdvisorById(req.authorAdvisorId())
                .orElseThrow(() -> new NotFoundException("Asesora no encontrada con ID: " + req.authorAdvisorId()));
        post.setAuthorAdvisorId(req.authorAdvisorId());

        post.setReadingTimeMinutes(req.readingTimeMinutes() != null ? req.readingTimeMinutes() : 5);
        post.setTags(req.tags() != null ? req.tags() : new ArrayList<>());
        post.setStatus(req.status() != null ? req.status() : "PUBLISHED");
        post.setPublishedAt("PUBLISHED".equalsIgnoreCase(req.status()) ? Instant.now() : null);
        post.setViewCount(0L);
        post.setIsFeatured(req.isFeatured() != null ? req.isFeatured() : false);
        post.setActive(req.active() != null ? req.active() : true);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        BlogPost saved = postRepository.save(post);

        // Post único (respuesta de creación, no un listado): se resuelve con el mismo
        // contrato batch usando una lista de un solo elemento y se devuelve con el
        // markdown completo, ya que es la confirmación de lo que se acaba de guardar.
        List<BlogPost> savedAsList = List.of(saved);
        Map<Long, MediaAsset> mediaById = ListPublicBlogPostsUseCase.resolveMediaMap(savedAsList, mediaRepository);
        Map<Long, TravelAdvisor> advisorById = ListPublicBlogPostsUseCase.resolveAdvisorMap(savedAsList, travelAdvisorRepository);
        return ListPublicBlogPostsUseCase.mapToDTO(saved, mediaById, advisorById, true);
    }
}
