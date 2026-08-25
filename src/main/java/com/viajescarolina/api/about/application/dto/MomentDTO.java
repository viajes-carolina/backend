package com.viajescarolina.api.about.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Un momento numerado de la sección "Experiencias que humanizan". */
public record MomentDTO(
    @NotBlank(message = "El título del momento es obligatorio")
    String title,

    @NotBlank(message = "El cuerpo del momento es obligatorio")
    String body
) {}
