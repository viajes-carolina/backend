package com.viajescarolina.api.about.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateAboutPageRequest(
    // Hero
    @NotBlank(message = "El badge del hero es obligatorio")
    String heroBadge,

    @NotBlank(message = "El título del hero es obligatorio")
    String heroTitle,

    @NotBlank(message = "El subtítulo del hero es obligatorio")
    String heroSubtitle,

    Long heroMediaId,

    @DecimalMin(value = "0", message = "El foco horizontal del hero debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "El foco horizontal del hero debe ser menor o igual a 100")
    Double heroFocalX,

    @DecimalMin(value = "0", message = "El foco vertical del hero debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "El foco vertical del hero debe ser menor o igual a 100")
    Double heroFocalY,

    @NotBlank(message = "El badge de la tarjeta del hero es obligatorio")
    String heroCardBadge,

    @NotBlank(message = "El título de la tarjeta del hero es obligatorio")
    String heroCardTitle,

    @NotBlank(message = "El texto de la nota del hero es obligatorio")
    String heroNoteText,

    // Historia
    @NotBlank(message = "El título de la historia es obligatorio")
    String storyTitle,

    @NotBlank(message = "El cuerpo de la historia es obligatorio")
    String storyBody,

    Long storyMediaId,

    @DecimalMin(value = "0", message = "El foco horizontal de la historia debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "El foco horizontal de la historia debe ser menor o igual a 100")
    Double storyFocalX,

    @DecimalMin(value = "0", message = "El foco vertical de la historia debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "El foco vertical de la historia debe ser menor o igual a 100")
    Double storyFocalY,

    // Misión
    @NotBlank(message = "El título de la misión es obligatorio")
    String missionTitle,

    @NotBlank(message = "El cuerpo de la misión es obligatorio")
    String missionBody,

    @NotBlank(message = "La cita de la misión es obligatoria")
    String missionQuote,

    @NotNull(message = "La ruta de pasos es obligatoria")
    @Valid
    List<JourneyStepDTO> journeySteps,

    @NotNull(message = "Los valores son obligatorios")
    List<String> values,

    // Cómo te acompañamos
    @NotBlank(message = "El badge de 'Cómo te acompañamos' es obligatorio")
    String accompanyBadge,

    @NotBlank(message = "El título de 'Cómo te acompañamos' es obligatorio")
    String accompanyTitle,

    @NotBlank(message = "El subtítulo de 'Cómo te acompañamos' es obligatorio")
    String accompanySubtitle,

    @NotNull(message = "Los pasos de acompañamiento son obligatorios")
    @Valid
    List<AccompanyStepDTO> accompanySteps,

    @NotBlank(message = "La cita de acompañamiento es obligatoria")
    String accompanyQuote,

    @NotBlank(message = "La atribución de la cita de acompañamiento es obligatoria")
    String accompanyQuoteAttribution,

    // Experiencias que humanizan
    @NotBlank(message = "El badge de 'Experiencias que humanizan' es obligatorio")
    String momentsBadge,

    @NotBlank(message = "El título de 'Experiencias que humanizan' es obligatorio")
    String momentsTitle,

    @NotBlank(message = "El subtítulo de 'Experiencias que humanizan' es obligatorio")
    String momentsSubtitle,

    Long momentsMediaId,

    @DecimalMin(value = "0", message = "El foco horizontal de experiencias debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "El foco horizontal de experiencias debe ser menor o igual a 100")
    Double momentsFocalX,

    @DecimalMin(value = "0", message = "El foco vertical de experiencias debe ser mayor o igual a 0")
    @DecimalMax(value = "100", message = "El foco vertical de experiencias debe ser menor o igual a 100")
    Double momentsFocalY,

    @NotNull(message = "Los momentos son obligatorios")
    @Valid
    List<MomentDTO> moments,

    // Una persona al otro lado
    @NotBlank(message = "El badge de 'Una persona al otro lado' es obligatorio")
    String humanBadge,

    @NotBlank(message = "El título de 'Una persona al otro lado' es obligatorio")
    String humanTitle,

    @NotBlank(message = "El subtítulo de 'Una persona al otro lado' es obligatorio")
    String humanSubtitle,

    @NotBlank(message = "El tagline de 'Una persona al otro lado' es obligatorio")
    String humanTagline
) {}
