package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDTO;
import com.viajescarolina.api.blog.application.dto.PublicBlogResponse;
import com.viajescarolina.api.blog.domain.BlogCategory;
import com.viajescarolina.api.blog.domain.BlogCategoryRepository;
import com.viajescarolina.api.blog.domain.BlogPost;
import com.viajescarolina.api.blog.domain.BlogPostRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListPublicBlogPostsUseCase {

    @Inject
    BlogPostRepository postRepository;

    @Inject
    BlogCategoryRepository categoryRepository;

    @Inject
    MediaRepository mediaRepository;

    public PublicBlogResponse execute(String categorySlug, String search, int page, int size) {
        List<BlogPost> posts = postRepository.findPublicPosts(categorySlug, search, null, page, size);
        long total = postRepository.countPublicPosts(categorySlug, search, null);
        int totalPages = (int) Math.ceil((double) total / (size > 0 ? size : 10));

        List<BlogPostDTO> postDTOs = posts.stream().map(p -> mapToDTO(p, mediaRepository)).toList();

        List<BlogCategory> categories = categoryRepository.findAllActive();
        List<BlogCategoryDTO> categoryDTOs = categories.stream().map(this::toCategoryDTO).toList();

        // Get featured post if no specific search/filter is active
        BlogPostDTO featuredDTO = null;
        if ((categorySlug == null || categorySlug.isBlank()) && (search == null || search.isBlank())) {
            List<BlogPost> featuredList = postRepository.findPublicPosts(null, null, true, 0, 1);
            if (!featuredList.isEmpty()) {
                featuredDTO = mapToDTO(featuredList.getFirst(), mediaRepository);
            }
        }

        return new PublicBlogResponse(postDTOs, categoryDTOs, featuredDTO, total, page, size, totalPages);
    }

    public static BlogPostDTO mapToDTO(BlogPost p, MediaRepository mediaRepository) {
        String coverUrl = p.getCoverMediaId() != null
                ? mediaRepository.findMediaById(p.getCoverMediaId()).map(MediaAsset::getStoragePath).orElse(null)
                : null;
        String avatarUrl = p.getAuthorAvatarMediaId() != null
                ? mediaRepository.findMediaById(p.getAuthorAvatarMediaId()).map(MediaAsset::getStoragePath).orElse(null)
                : null;
        return new BlogPostDTO(
                p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), p.getContentMarkdown(),
                p.getCategoryId(), p.getCategoryName(), p.getCategorySlug(),
                p.getCoverMediaId(), coverUrl,
                p.getCoverFocalX() != null ? p.getCoverFocalX() : 50.0,
                p.getCoverFocalY() != null ? p.getCoverFocalY() : 50.0,
                p.getAuthorName(), p.getAuthorAvatarMediaId(), avatarUrl,
                p.getAuthorAvatarFocalX() != null ? p.getAuthorAvatarFocalX() : 50.0,
                p.getAuthorAvatarFocalY() != null ? p.getAuthorAvatarFocalY() : 50.0,
                p.getReadingTimeMinutes(), p.getTags(), p.getStatus(), p.getPublishedAt(),
                p.getViewCount(), p.getIsFeatured(), p.getActive(), p.getCreatedAt(), p.getUpdatedAt()
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
