package com.viajescarolina.api.trust.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateFaqRequest(
        @NotBlank(message = "La pregunta es obligatoria")
        String question,

        @NotBlank(message = "La respuesta es obligatoria")
        String answer,

        String category,
        Integer displayOrder,
        Boolean active
) {}
