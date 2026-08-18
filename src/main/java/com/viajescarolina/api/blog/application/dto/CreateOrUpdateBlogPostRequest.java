package com.viajescarolina.api.blog.application.dto;

import java.util.List;

public record CreateOrUpdateBlogPostRequest(
        String slug,
        String title,
        String summary,
        String contentMarkdown,
        Long categoryId,
        Long coverMediaId,
        String authorName,
        Integer readingTimeMinutes,
        List<String> tags,
        String status,
        Boolean isFeatured,
        Boolean active) {
}
