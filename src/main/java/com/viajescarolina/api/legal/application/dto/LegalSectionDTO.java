package com.viajescarolina.api.legal.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Ítem {title, body} de una sección numerada. Reutilizado por las 5 páginas legales. */
public record LegalSectionDTO(
    @NotBlank(message = "El título de la sección es obligatorio")
    String title,

    @NotBlank(message = "El cuerpo de la sección es obligatorio")
    String body
) {}
