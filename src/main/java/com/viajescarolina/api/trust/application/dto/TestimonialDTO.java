package com.viajescarolina.api.trust.application.dto;

import java.time.Instant;

public record TestimonialDTO(
        Long id,
        String clientName,
        String clientLocation,
        String tripDestination,
        String comment,
        Integer rating,
        Long avatarMediaId,
        String avatarMediaUrl,
        boolean consentConfirmed,
        Integer displayOrder,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
