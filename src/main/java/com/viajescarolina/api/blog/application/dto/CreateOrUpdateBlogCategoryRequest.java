package com.viajescarolina.api.blog.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateBlogCategoryRequest(
        @NotBlank(message = "El nombre de la categoría es obligatorio")
        String name,

        @NotBlank(message = "El slug de la categoría es obligatorio")
        String slug,

        String description,
        Integer displayOrder,
        Boolean active) {
}
