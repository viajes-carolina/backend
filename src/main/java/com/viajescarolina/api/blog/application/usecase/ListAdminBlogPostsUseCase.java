package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListAdminBlogPostsUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    MediaRepository mediaRepository;

    public List<BlogPostDTO> execute(String status, String search, int page, int size) {
        List<BlogPost> posts = postRepository.findAllAdmin(status, search, page, size);
        return posts.stream().map(p -> ListPublicBlogPostsUseCase.mapToDTO(p, mediaRepository)).toList();
    }
}
