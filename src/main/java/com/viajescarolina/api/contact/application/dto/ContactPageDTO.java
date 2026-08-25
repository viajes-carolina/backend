package com.viajescarolina.api.contact.application.dto;

import java.time.Instant;
import java.util.List;

public record ContactPageDTO(
    Integer id,

    // Hero
    String heroBadge,
    String heroTitle,
    String heroSubtitle,
    String heroCtaText,
    String heroNoteText,
    String heroCtaMessage,
    String heroChatLabel,
    String heroChatBubble1,
    String heroChatBubble2,
    String heroChatBubble3,

    // Cómo empezar
    String startersBadge,
    String startersTitle,
    String startersSubtitle,
    String startersClosing,
    List<StarterPhraseDTO> starterPhrases,

    // Oficina y Google Maps
    String officeSectionBadge,
    String officeSectionTitle,
    String officeSectionSubtitle,
    String officeMapTitle,
    String officeMapSubtitle,
    String officeVisitNote,
    String officeMapEyebrow,
    String officeMapPinTitle,
    String officeMapPinSubtitle,
    String officeMapsLinkText,
    String officeLocationLabel,
    String officeVisitLabel,
    String officeVisitCtaText,
    String officeVisitCtaMessage,

    int revision,
    Instant updatedAt
) {}
