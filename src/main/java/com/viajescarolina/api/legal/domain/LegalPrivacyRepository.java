package com.viajescarolina.api.legal.domain;

import java.util.Optional;

public interface LegalPrivacyRepository {
    Optional<LegalPrivacy> findSingleton();
    LegalPrivacy save(LegalPrivacy page);
}
