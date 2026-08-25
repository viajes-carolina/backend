package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDetailResponse;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class GetBlogPostBySlugUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    MediaRepository mediaRepository;

    public BlogPostDetailResponse execute(String slug) {
        BlogPost post = postRepository.findPublicBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Artículo de blog no encontrado: " + slug));

        // Increment view count
        postRepository.incrementViewCount(post.getId());
        post.setViewCount(post.getViewCount() + 1);

        // Related posts in the same category
        List<BlogPost> related = postRepository.findRelatedPosts(post.getCategoryId(), post.getId(), 3);
        List<BlogPostDTO> relatedDTOs = related.stream().map(p -> ListPublicBlogPostsUseCase.mapToDTO(p, mediaRepository)).toList();

        return new BlogPostDetailResponse(ListPublicBlogPostsUseCase.mapToDTO(post, mediaRepository), relatedDTOs);
    }
}
