package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.application.dto.CreateOrUpdateBlogCategoryRequest;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;

@ApplicationScoped
public class CreateBlogCategoryUseCase {

    @Inject
    BlogCategoryRepository categoryRepository;

    @Audited(action = "CREATE_BLOG_CATEGORY", entityType = "BLOG_CATEGORY")
    @Transactional
    public BlogCategoryDTO execute(CreateOrUpdateBlogCategoryRequest req) {
        BlogCategory category = new BlogCategory();
        category.setName(req.name().trim());
        category.setSlug(req.slug().trim().toLowerCase());
        category.setDescription(req.description() != null ? req.description().trim() : "");
        category.setDisplayOrder(req.displayOrder() != null ? req.displayOrder() : 1);
        category.setActive(req.active() != null ? req.active() : true);
        category.setCreatedAt(Instant.now());
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
