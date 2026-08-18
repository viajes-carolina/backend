package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.PublicSiteResponse;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import com.viajescarolina.api.settings.domain.WhatsAppAction;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetPublicSiteSettingsUseCase {

    private final SettingsRepository settingsRepository;
    private final WhatsAppRepository whatsAppRepository;

    @Inject
    public GetPublicSiteSettingsUseCase(SettingsRepository settingsRepository, WhatsAppRepository whatsAppRepository) {
        this.settingsRepository = settingsRepository;
        this.whatsAppRepository = whatsAppRepository;
    }

    public PublicSiteResponse execute() {
        SiteSettings settings = settingsRepository.findSiteSettings()
                .orElseGet(() -> new SiteSettings(1, "Viajes Carolina", "El viaje comienza aquí", "contacto@viajescarolina.com", "+51 987 654 321", null, null, null, null, null, 1, null, null));

        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(1, "+51987654321", "+51 987 654 321", true, 1, null, null));

        Map<String, String> actionTemplates = whatsAppRepository.findAllActions().stream()
                .collect(Collectors.toMap(WhatsAppAction::getActionKey, WhatsAppAction::getMessageTemplate));

        return new PublicSiteResponse(
                settings.getSiteName(),
                settings.getBrandTagline(),
                settings.getContactEmail(),
                settings.getPrimaryPhone(),
                channel.isActive() ? channel.getE164Number() : "",
                channel.isActive() ? channel.getDisplayNumber() : "",
                settings.getFacebookUrl(),
                settings.getInstagramUrl(),
                settings.getTiktokUrl(),
                settings.getLogoMediaId(),
                settings.getFaviconMediaId(),
                actionTemplates
        );
    }
}
