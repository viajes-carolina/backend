package com.viajescarolina.api.blog.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrUpdateBlogPostRequest(
        @NotBlank(message = "El slug del artículo es obligatorio")
        String slug,

        @NotBlank(message = "El título del artículo es obligatorio")
        String title,

        @NotBlank(message = "El resumen del artículo es obligatorio")
        String summary,

        @NotBlank(message = "El contenido del artículo es obligatorio")
        String contentMarkdown,

        @NotNull(message = "La categoría del artículo es obligatoria")
        Long categoryId,

        Long coverMediaId,

        @DecimalMin(value = "0", message = "El foco horizontal de la portada debe ser mayor o igual a 0")
        @DecimalMax(value = "100", message = "El foco horizontal de la portada debe ser menor o igual a 100")
        Double coverFocalX,

        @DecimalMin(value = "0", message = "El foco vertical de la portada debe ser mayor o igual a 0")
        @DecimalMax(value = "100", message = "El foco vertical de la portada debe ser menor o igual a 100")
        Double coverFocalY,

        String authorName,

        Long authorAvatarMediaId,

        @DecimalMin(value = "0", message = "El foco horizontal del avatar debe ser mayor o igual a 0")
        @DecimalMax(value = "100", message = "El foco horizontal del avatar debe ser menor o igual a 100")
        Double authorAvatarFocalX,

        @DecimalMin(value = "0", message = "El foco vertical del avatar debe ser mayor o igual a 0")
        @DecimalMax(value = "100", message = "El foco vertical del avatar debe ser menor o igual a 100")
        Double authorAvatarFocalY,

        Integer readingTimeMinutes,
        List<String> tags,
        String status,
        Boolean isFeatured,
        Boolean active) {
}
