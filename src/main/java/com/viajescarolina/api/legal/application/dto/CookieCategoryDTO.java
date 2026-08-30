package com.viajescarolina.api.legal.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Categoría del panel de preferencias de cookies (Esenciales/Analítica/Preferencias). */
public record CookieCategoryDTO(
    @NotBlank(message = "La clave de la categoría es obligatoria")
    String key,

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    String name,

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    String description,

    boolean required
) {}
