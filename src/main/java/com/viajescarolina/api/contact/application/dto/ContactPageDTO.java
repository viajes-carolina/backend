package com.viajescarolina.api.contact.application.dto;

import java.time.Instant;

public record ContactPageDTO(
    Integer id,

    // Hero
    String heroBadge,
    String heroTitle,
    String heroSubtitle,
    String heroCtaText,
    String heroNoteText,
    String heroCtaMessage,
    String heroInfoTitle,
    String heroInfoWhatsappLabel,
    String heroInfoWhatsappValue,
    String heroInfoEmailLabel,
    String heroInfoScheduleLabel,
    String heroInfoOfficeLabel,

    // Oficina y Google Maps
    String officeSectionBadge,
    String officeSectionTitle,
    String officeMapTitle,
    String officeVisitNote,
    String officeMapEyebrow,
    String officeMapPinTitle,
    String officeMapPinSubtitle,
    String officeMapsLinkText,
    String officeVisitLabel,

    int revision,
    Instant updatedAt
) {}
