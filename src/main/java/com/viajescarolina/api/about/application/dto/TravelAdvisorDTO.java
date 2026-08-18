package com.viajescarolina.api.about.application.dto;

import java.time.Instant;

public record TravelAdvisorDTO(
    Long id,
    String fullName,
    String roleTitle,
    String specialty,
    String bio,
    Long photoMediaId,
    String photoMediaUrl,
    String whatsappPhone,
    String whatsappMessageTemplate,
    int displayOrder,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}
