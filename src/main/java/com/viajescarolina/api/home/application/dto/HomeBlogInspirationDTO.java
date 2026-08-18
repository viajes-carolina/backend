package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomeBlogInspiration;

public record HomeBlogInspirationDTO(
        Long id,
        String badgeText,
        String titleHighlight,
        String titleAccent,
        String subtitle,
        String ctaText,
        String ctaUrl,
        Integer postsLimit,
        Boolean active
) {
    public static HomeBlogInspirationDTO fromDomain(HomeBlogInspiration domain) {
        if (domain == null) return null;
        return new HomeBlogInspirationDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitleHighlight(),
                domain.getTitleAccent(),
                domain.getSubtitle(),
                domain.getCtaText(),
                domain.getCtaUrl(),
                domain.getPostsLimit(),
                domain.getActive()
        );
    }
}
