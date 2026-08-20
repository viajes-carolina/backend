package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteBlogPostUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Audited(action = "ARCHIVE_BLOG_POST", entityType = "BLOG_POST")
    @Transactional
    public void execute(Long id) {
        postRepository.delete(id);
    }
}
