package com.viajescarolina.api.home.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public record UpdateHomeHeroRequest(
        @NotBlank(message = "El texto de la insignia es obligatorio")
        String badgeText,

        @NotBlank(message = "El título principal es obligatorio")
        String titleHighlight,

        @NotBlank(message = "El texto de acento del título es obligatorio")
        String titleAccent,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotBlank(message = "El texto del botón WhatsApp es obligatorio")
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
        String featuredCardMediaUrl
) {}
