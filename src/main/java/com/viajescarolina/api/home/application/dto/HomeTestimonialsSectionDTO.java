package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomeTestimonialsSection;

public record HomeTestimonialsSectionDTO(
        Long id,
        String badgeText,
        String title,
        String subtitle,
        Long blobMediaId,
        String blobMediaUrl,
        Double blobFocalX,
        Double blobFocalY,
        Long polaroidMediaId,
        String polaroidMediaUrl,
        Double polaroidFocalX,
        Double polaroidFocalY
) {
    // NOTA: no resuelve mediaId -> URL aquí (este método no tiene acceso a
    // MediaRepository) — esa resolución ocurre en el caso de uso, igual que
    // en GetPublicHomePromotionsSectionUseCase. Este fromDomain solo mapea
    // 1:1 lo que ya trae el dominio (usado cuando la URL ya viene resuelta).
    public static HomeTestimonialsSectionDTO fromDomain(HomeTestimonialsSection domain) {
        if (domain == null) return null;
        return new HomeTestimonialsSectionDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitle(),
                domain.getSubtitle(),
                domain.getBlobMediaId(),
                domain.getBlobMediaUrl(),
                domain.getBlobFocalX(),
                domain.getBlobFocalY(),
                domain.getPolaroidMediaId(),
                domain.getPolaroidMediaUrl(),
                domain.getPolaroidFocalX(),
                domain.getPolaroidFocalY()
        );
    }
}
