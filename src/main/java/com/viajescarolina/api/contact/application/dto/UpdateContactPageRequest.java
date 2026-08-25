package com.viajescarolina.api.contact.application.dto;

import jakarta.validation.constraints.NotBlank;

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

    @NotBlank(message = "El título de la tarjeta de información es obligatorio")
    String heroInfoTitle,

    @NotBlank(message = "La etiqueta de WhatsApp es obligatoria")
    String heroInfoWhatsappLabel,

    @NotBlank(message = "El valor de WhatsApp es obligatorio")
    String heroInfoWhatsappValue,

    @NotBlank(message = "La etiqueta de correo es obligatoria")
    String heroInfoEmailLabel,

    @NotBlank(message = "La etiqueta de horario es obligatoria")
    String heroInfoScheduleLabel,

    @NotBlank(message = "La etiqueta de oficina es obligatoria")
    String heroInfoOfficeLabel,

    // Oficina y Google Maps
    @NotBlank(message = "El badge de la sección de oficina es obligatorio")
    String officeSectionBadge,

    @NotBlank(message = "El título de la sección de oficina es obligatorio")
    String officeSectionTitle,

    @NotBlank(message = "El título del mapa es obligatorio")
    String officeMapTitle,

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

    @NotBlank(message = "La etiqueta de 'antes de venir' es obligatoria")
    String officeVisitLabel
) {}
