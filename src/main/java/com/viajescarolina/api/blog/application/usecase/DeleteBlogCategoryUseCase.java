package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteBlogCategoryUseCase {

    @Inject
    BlogCategoryRepository categoryRepository;

    @Transactional
    public void execute(Long id) {
        categoryRepository.delete(id);
    }
}
