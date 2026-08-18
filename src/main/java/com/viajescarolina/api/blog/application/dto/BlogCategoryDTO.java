package com.viajescarolina.api.blog.application.dto;

import java.time.Instant;

public record BlogCategoryDTO(
        Long id,
        String name,
        String slug,
        String description,
        Integer displayOrder,
        Boolean active,
        Instant createdAt,
        Instant updatedAt) {
}
