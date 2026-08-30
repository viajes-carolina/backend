package com.viajescarolina.api.legal.domain;

import java.util.Optional;

public interface LegalTermsRepository {
    Optional<LegalTerms> findSingleton();
    LegalTerms save(LegalTerms page);
}
