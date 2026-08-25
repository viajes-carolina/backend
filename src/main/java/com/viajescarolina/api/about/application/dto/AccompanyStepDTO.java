package com.viajescarolina.api.about.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Un paso de la ruta vertical de la sección "Cómo te acompañamos". */
public record AccompanyStepDTO(
    @NotBlank(message = "El título del paso es obligatorio")
    String title,

    @NotBlank(message = "El cuerpo del paso es obligatorio")
    String body
) {}
