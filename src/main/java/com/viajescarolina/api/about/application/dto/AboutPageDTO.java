package com.viajescarolina.api.about.application.dto;

import java.time.Instant;
import java.util.List;

public record AboutPageDTO(
    Integer id,
    String heroBadge,
    String heroTitle,
    String heroSubtitle,
    Long heroMediaId,
    String heroMediaUrl,
    String storyTitle,
    String storyBody,
    Long storyMediaId,
    String storyMediaUrl,
    String missionTitle,
    String missionBody,
    String visionTitle,
    String visionBody,
    List<String> values,
    int experienceYears,
    int happyTravelers,
    int destinationsCount,
    int satisfactionRatePercent,
    int revision,
    Instant updatedAt
) {}
