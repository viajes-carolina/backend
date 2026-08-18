package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.application.dto.CreateOrUpdateBlogCategoryRequest;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;

@ApplicationScoped
public class UpdateBlogCategoryUseCase {

    @Inject
    BlogCategoryRepository categoryRepository;

    @Transactional
    public BlogCategoryDTO execute(Long id, CreateOrUpdateBlogCategoryRequest req) {
        BlogCategory category = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        if (req.name() != null && !req.name().isBlank()) category.setName(req.name().trim());
        if (req.slug() != null && !req.slug().isBlank()) category.setSlug(req.slug().trim().toLowerCase());
        if (req.description() != null) category.setDescription(req.description().trim());
        if (req.displayOrder() != null) category.setDisplayOrder(req.displayOrder());
        if (req.active() != null) category.setActive(req.active());
        category.setUpdatedAt(Instant.now());

        BlogCategory saved = categoryRepository.save(category);
        return toDTO(saved);
    }

    private BlogCategoryDTO toDTO(BlogCategory c) {
        return new BlogCategoryDTO(
                c.getId(),
                c.getName(),
                c.getSlug(),
                c.getDescription(),
                c.getDisplayOrder(),
                c.getActive(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
