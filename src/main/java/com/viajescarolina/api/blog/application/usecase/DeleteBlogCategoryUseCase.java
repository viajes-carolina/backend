package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteBlogCategoryUseCase {

    @Inject
    BlogCategoryRepository categoryRepository;

    @Audited(action = "DELETE_BLOG_CATEGORY", entityType = "BLOG_CATEGORY")
    @Transactional
    public void execute(Long id) {
        categoryRepository.delete(id);
    }
}
