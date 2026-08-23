package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomeFaqSection;

public record HomeFaqSectionDTO(
        Long id,
        String badgeText,
        String title,
        String subtitle
) {
    public static HomeFaqSectionDTO fromDomain(HomeFaqSection domain) {
        if (domain == null) return null;
        return new HomeFaqSectionDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitle(),
                domain.getSubtitle()
        );
    }
}
