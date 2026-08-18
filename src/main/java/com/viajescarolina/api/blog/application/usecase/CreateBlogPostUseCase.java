package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.CreateOrUpdateBlogPostRequest;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;
import java.util.ArrayList;

@ApplicationScoped
public class CreateBlogPostUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    BlogCategoryRepository categoryRepository;

    @Transactional
    public BlogPostDTO execute(CreateOrUpdateBlogPostRequest req) {
        if (req.slug() == null || req.slug().isBlank()) {
            throw new BadRequestException("El slug del artículo es obligatorio");
        }
        if (req.title() == null || req.title().isBlank()) {
            throw new BadRequestException("El título del artículo es obligatorio");
        }

        BlogCategory category = categoryRepository.findCategoryById(req.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + req.categoryId()));

        BlogPost post = new BlogPost();
        post.setSlug(req.slug().trim().toLowerCase());
        post.setTitle(req.title().trim());
        post.setSummary(req.summary() != null ? req.summary().trim() : "");
        post.setContentMarkdown(req.contentMarkdown() != null ? req.contentMarkdown() : "");
        post.setCategoryId(category.getId());
        post.setCategoryName(category.getName());
        post.setCategorySlug(category.getSlug());
        post.setCoverMediaId(req.coverMediaId());
        post.setAuthorName(req.authorName() != null ? req.authorName().trim() : "Equipo Viajes Carolina");
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
        return toDTO(saved);
    }

    private BlogPostDTO toDTO(BlogPost p) {
        return new BlogPostDTO(
                p.getId(),
                p.getSlug(),
                p.getTitle(),
                p.getSummary(),
                p.getContentMarkdown(),
                p.getCategoryId(),
                p.getCategoryName(),
                p.getCategorySlug(),
                p.getCoverMediaId(),
                p.getCoverMediaUrl(),
                p.getAuthorName(),
                p.getReadingTimeMinutes(),
                p.getTags(),
                p.getStatus(),
                p.getPublishedAt(),
                p.getViewCount(),
                p.getIsFeatured(),
                p.getActive(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
