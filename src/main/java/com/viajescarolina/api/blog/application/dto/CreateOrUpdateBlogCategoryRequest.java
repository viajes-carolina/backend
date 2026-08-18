package com.viajescarolina.api.blog.application.dto;

public record CreateOrUpdateBlogCategoryRequest(
        String name,
        String slug,
        String description,
        Integer displayOrder,
        Boolean active) {
}
