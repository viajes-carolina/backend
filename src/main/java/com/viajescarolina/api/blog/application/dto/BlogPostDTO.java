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
        Long authorAdvisorId,
        String authorName,
        String authorAvatarUrl,
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
