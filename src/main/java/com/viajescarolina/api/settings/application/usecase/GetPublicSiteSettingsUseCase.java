package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.PublicSiteResponse;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
                .orElseGet(() -> new SiteSettings(1, "Viajes Carolina", "El viaje comienza aquí", "contacto@viajescarolina.com", null, null, null, null, null, "VIAJES CAROLINA S.A.C.", "20601234567", null, null, "Lima, Perú", 1, null, null));

        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(null, "Línea Principal", "+51987654321", "+51 987 654 321",
                        "Hola Viajes Carolina, deseo asesoría personalizada para mi próximo viaje.", true, true, 1, null, null));

        return new PublicSiteResponse(
                settings.getSiteName(),
                settings.getBrandTagline(),
                settings.getContactEmail(),
                channel.isActive() ? channel.getDisplayNumber() : "",
                channel.isActive() ? channel.getE164Number() : "",
                channel.isActive() ? channel.getDisplayNumber() : "",
                settings.getFacebookUrl(),
                settings.getInstagramUrl(),
                settings.getTiktokUrl(),
                settings.getLogoMediaId(),
                settings.getFaviconMediaId(),
                settings.getLegalCompanyName(),
                settings.getTaxId(),
                settings.getMinceturCertificateUrl(),
                settings.getMinceturRegistrationNumber(),
                settings.getMinceturLocation()
        );
    }
}
