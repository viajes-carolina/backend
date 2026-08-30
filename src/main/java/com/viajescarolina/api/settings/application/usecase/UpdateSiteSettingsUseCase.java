package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.SiteSettingsDTO;
import com.viajescarolina.api.settings.application.dto.UpdateSiteSettingsRequest;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import com.viajescarolina.api.settings.domain.WhatsAppChannel;
import com.viajescarolina.api.settings.domain.WhatsAppRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateSiteSettingsUseCase {

    private final SettingsRepository settingsRepository;
    private final WhatsAppRepository whatsAppRepository;

    @Inject
    public UpdateSiteSettingsUseCase(SettingsRepository settingsRepository, WhatsAppRepository whatsAppRepository) {
        this.settingsRepository = settingsRepository;
        this.whatsAppRepository = whatsAppRepository;
    }

    @Audited(action = "UPDATE_SETTINGS", entityType = "SITE_SETTINGS")
    @Transactional
    public SiteSettingsDTO execute(UpdateSiteSettingsRequest request) {
        SiteSettings settings = settingsRepository.findSiteSettings()
                .orElseGet(() -> new SiteSettings(1, request.siteName(), request.brandTagline(), request.contactEmail(), request.logoMediaId(), request.faviconMediaId(), request.facebookUrl(), request.instagramUrl(), request.tiktokUrl(), request.legalCompanyName(), request.taxId(), request.minceturCertificateUrl(), request.minceturRegistrationNumber(), request.minceturLocation(), 0, null, null));

        settings.update(
                request.siteName(),
                request.brandTagline(),
                request.contactEmail(),
                request.logoMediaId(),
                request.faviconMediaId(),
                request.facebookUrl(),
                request.instagramUrl(),
                request.tiktokUrl(),
                request.legalCompanyName(),
                request.taxId(),
                request.minceturCertificateUrl(),
                request.minceturRegistrationNumber(),
                request.minceturLocation()
        );
        SiteSettings saved = settingsRepository.save(settings);

        WhatsAppChannel channel = whatsAppRepository.findChannel()
                .orElseGet(() -> new WhatsAppChannel(null, "Línea Principal", request.whatsappPhone(), request.whatsappDisplayNumber(),
                        request.whatsappDefaultMessage(), true, true, 0, null, null));
        channel.update(channel.getLabel(), request.whatsappPhone(), request.whatsappDisplayNumber(), request.whatsappDefaultMessage(), true);
        WhatsAppChannel savedChannel = whatsAppRepository.saveChannel(channel);

        return new SiteSettingsDTO(
                saved.getId(),
                saved.getSiteName(),
                saved.getBrandTagline(),
                saved.getContactEmail(),
                saved.getLogoMediaId(),
                saved.getFaviconMediaId(),
                saved.getFacebookUrl(),
                saved.getInstagramUrl(),
                saved.getTiktokUrl(),
                saved.getLegalCompanyName(),
                saved.getTaxId(),
                saved.getMinceturCertificateUrl(),
                saved.getMinceturRegistrationNumber(),
                saved.getMinceturLocation(),
                savedChannel.getDisplayNumber(),
                savedChannel.getE164Number(),
                savedChannel.getDisplayNumber(),
                savedChannel.getDefaultMessage(),
                saved.getRevision(),
                saved.getUpdatedAt()
        );
    }
}
