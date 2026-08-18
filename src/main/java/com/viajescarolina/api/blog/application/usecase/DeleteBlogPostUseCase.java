package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.domain.BlogPostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteBlogPostUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Transactional
    public void execute(Long id) {
        postRepository.delete(id);
    }
}
