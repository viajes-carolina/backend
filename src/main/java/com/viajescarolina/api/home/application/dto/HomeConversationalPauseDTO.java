package com.viajescarolina.api.home.application.dto;

import com.viajescarolina.api.home.domain.HomeConversationalPause;
import java.util.List;

public record HomeConversationalPauseDTO(
        Long id,
        String badgeText,
        String title,
        String subtitle,
        String whatsappCtaText,
        String whatsappMessageTemplate,
        String financingEyebrowText,
        Integer financingInstallmentsCount,
        String financingDisclaimerText,
        List<String> financingBanks
) {
    public static HomeConversationalPauseDTO fromDomain(HomeConversationalPause domain) {
        if (domain == null) return null;
        return new HomeConversationalPauseDTO(
                domain.getId(),
                domain.getBadgeText(),
                domain.getTitle(),
                domain.getSubtitle(),
                domain.getWhatsappCtaText(),
                domain.getWhatsappMessageTemplate(),
                domain.getFinancingEyebrowText(),
                domain.getFinancingInstallmentsCount(),
                domain.getFinancingDisclaimerText(),
                domain.getFinancingBanks()
        );
    }
}
