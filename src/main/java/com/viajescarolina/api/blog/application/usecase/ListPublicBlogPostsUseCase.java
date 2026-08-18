package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.PublicBlogResponse;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListPublicBlogPostsUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    BlogCategoryRepository categoryRepository;

    public PublicBlogResponse execute(String categorySlug, String search, int page, int size) {
        List<BlogPost> posts = postRepository.findPublicPosts(categorySlug, search, null, page, size);
        long total = postRepository.countPublicPosts(categorySlug, search, null);
        int totalPages = (int) Math.ceil((double) total / (size > 0 ? size : 10));

        List<BlogPostDTO> postDTOs = posts.stream().map(this::toDTO).toList();

        List<BlogCategory> categories = categoryRepository.findAllActive();
        List<BlogCategoryDTO> categoryDTOs = categories.stream().map(this::toCategoryDTO).toList();

        // Get featured post if no specific search/filter is active
        BlogPostDTO featuredDTO = null;
        if ((categorySlug == null || categorySlug.isBlank()) && (search == null || search.isBlank())) {
            List<BlogPost> featuredList = postRepository.findPublicPosts(null, null, true, 0, 1);
            if (!featuredList.isEmpty()) {
                featuredDTO = toDTO(featuredList.getFirst());
            }
        }

        return new PublicBlogResponse(postDTOs, categoryDTOs, featuredDTO, total, page, size, totalPages);
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

    private BlogCategoryDTO toCategoryDTO(BlogCategory c) {
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
