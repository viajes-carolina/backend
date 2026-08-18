package com.viajescarolina.api.trust.application.dto;

import java.time.Instant;

public record FaqItemDTO(
        Long id,
        String question,
        String answer,
        String category,
        Integer displayOrder,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
