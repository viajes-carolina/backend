package com.viajescarolina.api.about.application.dto;

import java.time.Instant;
import java.util.List;

public record AboutPageDTO(
    Integer id,

    // Hero
    String heroBadge,
    String heroTitle,
    String heroSubtitle,
    Long heroMediaId,
    String heroMediaUrl,
    Double heroFocalX,
    Double heroFocalY,
    String heroCardBadge,
    String heroCardTitle,
    String heroNoteText,

    // Historia
    String storyTitle,
    String storyBody,
    Long storyMediaId,
    String storyMediaUrl,
    Double storyFocalX,
    Double storyFocalY,

    // Misión
    String missionTitle,
    String missionBody,
    String missionQuote,
    List<JourneyStepDTO> journeySteps,

    List<String> values,

    // Cómo te acompañamos
    String accompanyBadge,
    String accompanyTitle,
    String accompanySubtitle,
    List<AccompanyStepDTO> accompanySteps,
    String accompanyQuote,
    String accompanyQuoteAttribution,

    // Experiencias que humanizan
    String momentsBadge,
    String momentsTitle,
    String momentsSubtitle,
    Long momentsMediaId,
    String momentsMediaUrl,
    Double momentsFocalX,
    Double momentsFocalY,
    List<MomentDTO> moments,

    // Una persona al otro lado
    String humanBadge,
    String humanTitle,
    String humanSubtitle,
    String humanTagline,

    int revision,
    Instant updatedAt
) {}
