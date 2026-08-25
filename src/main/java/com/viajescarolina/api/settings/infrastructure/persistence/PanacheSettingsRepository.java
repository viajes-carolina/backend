package com.viajescarolina.api.settings.infrastructure.persistence;

import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheSettingsRepository implements SettingsRepository {

    @Override
    public Optional<SiteSettings> findSiteSettings() {
        SiteSettingsPanacheEntity entity = SiteSettingsPanacheEntity.findById(1);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
    }

    @Override
    public SiteSettings save(SiteSettings settings) {
        SiteSettingsPanacheEntity entity = SiteSettingsPanacheEntity.findById(1);
        if (entity == null) {
            entity = new SiteSettingsPanacheEntity();
            entity.id = 1;
        }

        entity.siteName = settings.getSiteName();
        entity.brandTagline = settings.getBrandTagline();
        entity.contactEmail = settings.getContactEmail();
        entity.logoMediaId = settings.getLogoMediaId();
        entity.faviconMediaId = settings.getFaviconMediaId();
        entity.facebookUrl = settings.getFacebookUrl();
        entity.instagramUrl = settings.getInstagramUrl();
        entity.tiktokUrl = settings.getTiktokUrl();
        entity.legalCompanyName = settings.getLegalCompanyName();
        entity.taxId = settings.getTaxId();
        entity.minceturCertificateUrl = settings.getMinceturCertificateUrl();
        entity.revision = settings.getRevision();
        entity.updatedAt = settings.getUpdatedAt();

        entity.persist();
        return toDomain(entity);
    }

    private SiteSettings toDomain(SiteSettingsPanacheEntity entity) {
        return new SiteSettings(
                entity.id,
                entity.siteName,
                entity.brandTagline,
                entity.contactEmail,
                entity.logoMediaId,
                entity.faviconMediaId,
                entity.facebookUrl,
                entity.instagramUrl,
                entity.tiktokUrl,
                entity.legalCompanyName,
                entity.taxId,
                entity.minceturCertificateUrl,
                entity.revision,
                entity.createdAt,
                entity.updatedAt
        );
    }
}
