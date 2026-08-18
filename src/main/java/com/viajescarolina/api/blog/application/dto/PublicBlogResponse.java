package com.viajescarolina.api.blog.application.dto;

import java.util.List;

public record PublicBlogResponse(
        List<BlogPostDTO> items,
        List<BlogCategoryDTO> categories,
        BlogPostDTO featuredPost,
        long total,
        int page,
        int size,
        int totalPages) {
}
