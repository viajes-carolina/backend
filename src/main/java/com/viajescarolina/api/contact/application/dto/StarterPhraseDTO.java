package com.viajescarolina.api.contact.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Una frase de ejemplo de la sección "Cómo empezar". */
public record StarterPhraseDTO(
    @NotBlank(message = "La cita de la frase es obligatoria")
    String quote,

    @NotBlank(message = "El texto de apoyo de la frase es obligatorio")
    String support
) {}
