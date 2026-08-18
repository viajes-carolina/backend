package com.viajescarolina.api.intentions.application.dto;

import java.time.Instant;
import java.util.List;

public record TravelIntentionDTO(
        Long id,
        String slug,
        String title,
        String tagline,
        String iconName,
        List<String> featuredDestinations,
        String whatsappMessageTemplate,
        Long coverMediaId,
        String coverMediaUrl,
        Double coverFocalX,
        Double coverFocalY,
        Integer displayOrder,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
