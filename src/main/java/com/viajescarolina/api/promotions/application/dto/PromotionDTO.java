package com.viajescarolina.api.promotions.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PromotionDTO(
        Long id,
        String slug,
        String title,
        String destination,
        String summary,
        BigDecimal priceUsd,
        BigDecimal pricePen,
        Integer durationDays,
        Integer durationNights,
        String departureCity,
        LocalDate validFrom,
        LocalDate validUntil,
        Long featuredMediaId,
        String featuredMediaUrl,
        Double featuredMediaFocalX,
        Double featuredMediaFocalY,
        List<String> inclusions,
        List<String> exclusions,
        String whatsappMessageTemplate,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        String source,
        String facebookPostId,
        String facebookPermalinkUrl
) {}
