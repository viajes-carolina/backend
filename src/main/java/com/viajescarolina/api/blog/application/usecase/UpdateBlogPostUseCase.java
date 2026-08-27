package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.CreateOrUpdateBlogPostRequest;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.common.audit.Audited;
import com.viajescarolina.api.about.domain.TravelAdvisorRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;

@ApplicationScoped
public class UpdateBlogPostUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    BlogCategoryRepository categoryRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TravelAdvisorRepository travelAdvisorRepository;

    @Audited(action = "UPDATE_BLOG_POST", entityType = "BLOG_POST")
    @Transactional
    public BlogPostDTO execute(Long id, CreateOrUpdateBlogPostRequest req) {
        BlogPost post = postRepository.findPostById(id)
                .orElseThrow(() -> new NotFoundException("Artículo no encontrado con ID: " + id));

        if (req.categoryId() != null && !req.categoryId().equals(post.getCategoryId())) {
            BlogCategory category = categoryRepository.findCategoryById(req.categoryId())
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + req.categoryId()));
            post.setCategoryId(category.getId());
            post.setCategoryName(category.getName());
            post.setCategorySlug(category.getSlug());
        }

        if (req.slug() != null && !req.slug().isBlank()) post.setSlug(req.slug().trim().toLowerCase());
        if (req.title() != null && !req.title().isBlank()) post.setTitle(req.title().trim());
        if (req.summary() != null) post.setSummary(req.summary().trim());
        if (req.contentMarkdown() != null) post.setContentMarkdown(req.contentMarkdown());

        if (req.coverMediaId() != null) {
            Long coverMediaId = mediaRepository.findMediaById(req.coverMediaId()).isPresent()
                    ? req.coverMediaId() : null;
            post.setCoverMediaId(coverMediaId);
        }
        if (req.coverFocalX() != null) post.setCoverFocalX(req.coverFocalX());
        if (req.coverFocalY() != null) post.setCoverFocalY(req.coverFocalY());

        if (req.authorAdvisorId() != null) {
            travelAdvisorRepository.findAdvisorById(req.authorAdvisorId())
                    .orElseThrow(() -> new NotFoundException("Asesora no encontrada con ID: " + req.authorAdvisorId()));
            post.setAuthorAdvisorId(req.authorAdvisorId());
        }

        if (req.readingTimeMinutes() != null) post.setReadingTimeMinutes(req.readingTimeMinutes());
        if (req.tags() != null) post.setTags(req.tags());
        if (req.status() != null) {
            if (!req.status().equalsIgnoreCase(post.getStatus()) && "PUBLISHED".equalsIgnoreCase(req.status()) && post.getPublishedAt() == null) {
                post.setPublishedAt(Instant.now());
            }
            post.setStatus(req.status());
        }
        if (req.isFeatured() != null) post.setIsFeatured(req.isFeatured());
        if (req.active() != null) post.setActive(req.active());
        post.setUpdatedAt(Instant.now());

        BlogPost saved = postRepository.save(post);
        return ListPublicBlogPostsUseCase.mapToDTO(saved, mediaRepository, travelAdvisorRepository);
    }
}
