package com.viajescarolina.api.promotions.application.dto;

public record PromotionGalleryItemDTO(
        Long mediaId,
        String mediaUrl,
        Double focalX,
        Double focalY
) {}
