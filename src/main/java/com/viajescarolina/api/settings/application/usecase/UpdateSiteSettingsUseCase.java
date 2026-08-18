package com.viajescarolina.api.settings.application.usecase;

import com.viajescarolina.api.settings.application.dto.SiteSettingsDTO;
import com.viajescarolina.api.settings.application.dto.UpdateSiteSettingsRequest;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateSiteSettingsUseCase {

    private final SettingsRepository settingsRepository;

    @Inject
    public UpdateSiteSettingsUseCase(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Transactional
    public SiteSettingsDTO execute(UpdateSiteSettingsRequest request) {
        SiteSettings settings = settingsRepository.findSiteSettings()
                .orElseGet(() -> new SiteSettings(1, request.siteName(), request.brandTagline(), request.contactEmail(), request.primaryPhone(), request.logoMediaId(), request.faviconMediaId(), request.facebookUrl(), request.instagramUrl(), request.tiktokUrl(), 0, null, null));

        settings.update(
                request.siteName(),
                request.brandTagline(),
                request.contactEmail(),
                request.primaryPhone(),
                request.logoMediaId(),
                request.faviconMediaId(),
                request.facebookUrl(),
                request.instagramUrl(),
                request.tiktokUrl()
        );

        SiteSettings saved = settingsRepository.save(settings);

        return new SiteSettingsDTO(
                saved.getId(),
                saved.getSiteName(),
                saved.getBrandTagline(),
                saved.getContactEmail(),
                saved.getPrimaryPhone(),
                saved.getLogoMediaId(),
                saved.getFaviconMediaId(),
                saved.getFacebookUrl(),
                saved.getInstagramUrl(),
                saved.getTiktokUrl(),
                saved.getRevision(),
                saved.getUpdatedAt()
        );
    }
}
