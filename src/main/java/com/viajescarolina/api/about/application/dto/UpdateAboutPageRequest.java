package com.viajescarolina.api.about.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateAboutPageRequest(
    @NotBlank String heroBadge,
    @NotBlank String heroTitle,
    @NotBlank String heroSubtitle,
    Long heroMediaId,
    @NotBlank String storyTitle,
    @NotBlank String storyBody,
    Long storyMediaId,
    @NotBlank String missionTitle,
    @NotBlank String missionBody,
    @NotBlank String visionTitle,
    @NotBlank String visionBody,
    @NotNull List<String> values,
    int experienceYears,
    int happyTravelers,
    int destinationsCount,
    int satisfactionRatePercent
) {}
