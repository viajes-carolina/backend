package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomeTestimonialsSection;

public record HomeTestimonialsSectionDTO(
        Long id,
        String badgeText,
        String title,
        String subtitle
) {
    public static HomeTestimonialsSectionDTO fromDomain(HomeTestimonialsSection domain) {
        if (domain == null) return null;
        return new HomeTestimonialsSectionDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitle(),
                domain.getSubtitle()
        );
    }
}
