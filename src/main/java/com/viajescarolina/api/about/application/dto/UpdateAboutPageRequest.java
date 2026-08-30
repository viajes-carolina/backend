package com.viajescarolina.api.about.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateAboutPageRequest(
    // Hero (01)
    @NotBlank(message = "El badge del hero es obligatorio")
    String heroBadge,

    @NotBlank(message = "El título del hero es obligatorio")
    String heroTitle,

    @NotBlank(message = "El subtítulo del hero es obligatorio")
    String heroSubtitle,

    @NotBlank(message = "El badge de la tarjeta del hero es obligatorio")
    String heroCardBadge,

    @NotBlank(message = "El título de la tarjeta del hero es obligatorio")
    String heroCardTitle,

    @NotBlank(message = "La ubicación de la tarjeta del hero es obligatoria")
    String heroCardLocation,

    @NotBlank(message = "El detalle de la tarjeta del hero es obligatorio")
    String heroCardDetail,

    @NotBlank(message = "El texto de la nota del hero es obligatorio")
    String heroNoteText,

    // Nuestra forma de trabajar (02)
    @NotBlank(message = "El badge de 'Nuestra forma de trabajar' es obligatorio")
    String accompanyBadge,

    @NotBlank(message = "El título de 'Nuestra forma de trabajar' es obligatorio")
    String accompanyTitle,

    @NotBlank(message = "El subtítulo de 'Nuestra forma de trabajar' es obligatorio")
    String accompanySubtitle,

    @NotNull(message = "Los pasos de acompañamiento son obligatorios")
    @Valid
    List<AccompanyStepDTO> accompanySteps,

    @NotBlank(message = "La cita de acompañamiento es obligatoria")
    String accompanyQuote,

    // Quién está detrás (03)
    @NotBlank(message = "El badge de 'Quién está detrás' es obligatorio")
    String advisorsBadge,

    @NotNull(message = "Los highlights de asesoras son obligatorios")
    @Valid
    List<AccompanyStepDTO> advisorsHighlights
) {}
