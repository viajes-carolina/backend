package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomePromotionsSection;

public record HomePromotionsSectionDTO(
        Long id,
        String badgeText,
        String title,
        String subtitle,
        String bottomCtaQuestion,
        String bottomCtaEyebrow,
        String bottomCtaCopy,
        String bottomCtaWhatsappText,
        String bottomCtaWhatsappMessage
) {
    public static HomePromotionsSectionDTO fromDomain(HomePromotionsSection domain) {
        if (domain == null) return null;
        return new HomePromotionsSectionDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitle(),
                domain.getSubtitle(),
                domain.getBottomCtaQuestion(),
                domain.getBottomCtaEyebrow(),
                domain.getBottomCtaCopy(),
                domain.getBottomCtaWhatsappText(),
                domain.getBottomCtaWhatsappMessage()
        );
    }
}
