package com.viajescarolina.api.about.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Item {title, body} reutilizado por "Nuestra forma de trabajar" (accompanySteps) y "Quién está detrás" (advisorsHighlights). */
public record AccompanyStepDTO(
    @NotBlank(message = "El título del paso es obligatorio")
    String title,

    @NotBlank(message = "El cuerpo del paso es obligatorio")
    String body
) {}
