package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDetailResponse;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class GetBlogPostBySlugUseCase {

    @Inject
    BlogPostRepository postRepository;

    public BlogPostDetailResponse execute(String slug) {
        BlogPost post = postRepository.findPublicBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Artículo de blog no encontrado: " + slug));

        // Increment view count
        postRepository.incrementViewCount(post.getId());
        post.setViewCount(post.getViewCount() + 1);

        // Related posts in the same category
        List<BlogPost> related = postRepository.findRelatedPosts(post.getCategoryId(), post.getId(), 3);
        List<BlogPostDTO> relatedDTOs = related.stream().map(this::toDTO).toList();

        return new BlogPostDetailResponse(toDTO(post), relatedDTOs);
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
