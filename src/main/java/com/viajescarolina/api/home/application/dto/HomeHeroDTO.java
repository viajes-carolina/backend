package com.viajescarolina.api.home.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record HomeHeroDTO(
        Integer id,
        String badgeText,
        String titleHighlight,
        String titleAccent,
        String description,
        String whatsappCtaText,
        String whatsappMessageOverride,
        String secondaryCtaText,
        String secondaryCtaUrl,
        List<String> trustIndicators,
        Long backgroundMediaId,
        String backgroundMediaUrl,
        Double backgroundFocalX,
        Double backgroundFocalY,
        String featuredCardBadge,
        String featuredCardTitle,
        String featuredCardSubtitle,
        BigDecimal featuredCardPricePen,
        String featuredCardOrigin,
        Long featuredCardMediaId,
        String featuredCardMediaUrl,
        Integer revision,
        Instant updatedAt
) {}
