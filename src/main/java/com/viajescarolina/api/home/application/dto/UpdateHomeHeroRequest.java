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
        String featuredCardMediaUrl,
        Long secondaryMedia1Id,
        String secondaryMedia1Url,
        Double secondaryMedia1FocalX,
        Double secondaryMedia1FocalY,
        Long secondaryMedia2Id,
        String secondaryMedia2Url,
        Double secondaryMedia2FocalX,
        Double secondaryMedia2FocalY,
        Long secondaryMedia3Id,
        String secondaryMedia3Url,
        Double secondaryMedia3FocalX,
        Double secondaryMedia3FocalY,
        Long secondaryMedia4Id,
        String secondaryMedia4Url,
        Double secondaryMedia4FocalX,
        Double secondaryMedia4FocalY,
        String trustStatText
) {}
