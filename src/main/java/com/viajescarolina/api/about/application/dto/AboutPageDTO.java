package com.viajescarolina.api.about.application.dto;

import java.time.Instant;
import java.util.List;

public record AboutPageDTO(
    Integer id,

    // Hero (01)
    String heroBadge,
    String heroTitle,
    String heroSubtitle,
    String heroCardBadge,
    String heroCardTitle,
    String heroCardLocation,
    String heroCardDetail,
    String heroNoteText,

    // Nuestra forma de trabajar (02)
    String accompanyBadge,
    String accompanyTitle,
    String accompanySubtitle,
    List<AccompanyStepDTO> accompanySteps,
    String accompanyQuote,

    // Quién está detrás (03)
    String advisorsBadge,
    List<AccompanyStepDTO> advisorsHighlights,

    int revision,
    Instant updatedAt
) {}
