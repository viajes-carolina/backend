package com.viajescarolina.api.blog.application.dto;

import java.time.Instant;
import java.util.List;

public record BlogPostDTO(
        Long id,
        String slug,
        String title,
        String summary,
        String contentMarkdown,
        Long categoryId,
        String categoryName,
        String categorySlug,
        Long coverMediaId,
        String coverMediaUrl,
        Double coverFocalX,
        Double coverFocalY,
        String authorName,
        Long authorAvatarMediaId,
        String authorAvatarUrl,
        Double authorAvatarFocalX,
        Double authorAvatarFocalY,
        Integer readingTimeMinutes,
        List<String> tags,
        String status,
        Instant publishedAt,
        Long viewCount,
        Boolean isFeatured,
        Boolean active,
        Instant createdAt,
        Instant updatedAt) {
}
