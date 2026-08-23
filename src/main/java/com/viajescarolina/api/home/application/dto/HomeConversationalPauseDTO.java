package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomeConversationalPause;

public record HomeConversationalPauseDTO(
        Long id,
        String badgeText,
        String title,
        String subtitle,
        String whatsappCtaText,
        String whatsappMessageTemplate
) {
    public static HomeConversationalPauseDTO fromDomain(HomeConversationalPause domain) {
        if (domain == null) return null;
        return new HomeConversationalPauseDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitle(),
                domain.getSubtitle(),
                domain.getWhatsappCtaText(),
                domain.getWhatsappMessageTemplate()
        );
    }
}
