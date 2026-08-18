package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListBlogCategoriesUseCase {

    @Inject
    BlogCategoryRepository categoryRepository;

    public List<BlogCategoryDTO> execute(boolean admin) {
        List<BlogCategory> list = admin ? categoryRepository.findAllAdmin() : categoryRepository.findAllActive();
        return list.stream().map(this::toDTO).toList();
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
