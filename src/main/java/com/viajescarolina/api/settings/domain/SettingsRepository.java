package com.viajescarolina.api.settings.domain;

import java.util.Optional;

public interface SettingsRepository {
    Optional<SiteSettings> findSiteSettings();
    SiteSettings save(SiteSettings settings);
}
