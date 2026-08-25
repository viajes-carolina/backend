package com.viajescarolina.api.contact.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateContactPageRequest(
    // Hero
    @NotBlank(message = "El badge del hero es obligatorio")
    String heroBadge,

    @NotBlank(message = "El título del hero es obligatorio")
    String heroTitle,

    @NotBlank(message = "El subtítulo del hero es obligatorio")
    String heroSubtitle,

    @NotBlank(message = "El texto del CTA del hero es obligatorio")
    String heroCtaText,

    @NotBlank(message = "El texto de la nota del hero es obligatorio")
    String heroNoteText,

    @NotBlank(message = "El mensaje de WhatsApp del hero es obligatorio")
    String heroCtaMessage,

    @NotBlank(message = "La etiqueta del chat del hero es obligatoria")
    String heroChatLabel,

    @NotBlank(message = "La burbuja 1 del chat del hero es obligatoria")
    String heroChatBubble1,

    @NotBlank(message = "La burbuja 2 del chat del hero es obligatoria")
    String heroChatBubble2,

    @NotBlank(message = "La burbuja 3 del chat del hero es obligatoria")
    String heroChatBubble3,

    // Cómo empezar
    @NotBlank(message = "El badge de 'Cómo empezar' es obligatorio")
    String startersBadge,

    @NotBlank(message = "El título de 'Cómo empezar' es obligatorio")
    String startersTitle,

    @NotBlank(message = "El subtítulo de 'Cómo empezar' es obligatorio")
    String startersSubtitle,

    @NotBlank(message = "El cierre de 'Cómo empezar' es obligatorio")
    String startersClosing,

    @NotNull(message = "Las frases de ejemplo son obligatorias")
    @Valid
    List<StarterPhraseDTO> starterPhrases,

    // Oficina y Google Maps
    @NotBlank(message = "El badge de la sección de oficina es obligatorio")
    String officeSectionBadge,

    @NotBlank(message = "El título de la sección de oficina es obligatorio")
    String officeSectionTitle,

    @NotBlank(message = "El subtítulo de la sección de oficina es obligatorio")
    String officeSectionSubtitle,

    @NotBlank(message = "El título del mapa es obligatorio")
    String officeMapTitle,

    @NotBlank(message = "El subtítulo del mapa es obligatorio")
    String officeMapSubtitle,

    @NotBlank(message = "La nota de visita es obligatoria")
    String officeVisitNote,

    @NotBlank(message = "El eyebrow del mapa es obligatorio")
    String officeMapEyebrow,

    @NotBlank(message = "El título del pin del mapa es obligatorio")
    String officeMapPinTitle,

    @NotBlank(message = "El subtítulo del pin del mapa es obligatorio")
    String officeMapPinSubtitle,

    @NotBlank(message = "El texto del link de Google Maps es obligatorio")
    String officeMapsLinkText,

    @NotBlank(message = "La etiqueta de ubicación es obligatoria")
    String officeLocationLabel,

    @NotBlank(message = "La etiqueta de 'antes de venir' es obligatoria")
    String officeVisitLabel,

    @NotBlank(message = "El texto del CTA de visita es obligatorio")
    String officeVisitCtaText,

    @NotBlank(message = "El mensaje de WhatsApp de visita es obligatorio")
    String officeVisitCtaMessage
) {}
