package com.viajescarolina.api.home.application.dto;

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
        String trustStatText,
        String eyebrowText,
        Integer revision,
        Instant updatedAt
) {}
