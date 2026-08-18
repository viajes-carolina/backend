package com.viajescarolina.api.settings.application.dto;

import java.time.Instant;

public record SiteSettingsDTO(
    Integer id,
    String siteName,
    String brandTagline,
    String contactEmail,
    String primaryPhone,
    Integer logoMediaId,
    Integer faviconMediaId,
    String facebookUrl,
    String instagramUrl,
    String tiktokUrl,
    int revision,
    Instant updatedAt
) {}
