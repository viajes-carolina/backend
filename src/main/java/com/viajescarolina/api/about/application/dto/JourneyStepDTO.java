package com.viajescarolina.api.about.application.dto;

import jakarta.validation.constraints.NotBlank;

/** Un paso de la ruta horizontal "de idea a recuerdo" de la sección Misión. */
public record JourneyStepDTO(
    @NotBlank(message = "La etiqueta del paso es obligatoria")
    String label
) {}
