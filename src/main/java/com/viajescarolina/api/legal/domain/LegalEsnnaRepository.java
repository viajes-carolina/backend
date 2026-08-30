package com.viajescarolina.api.legal.domain;

import java.util.Optional;

public interface LegalEsnnaRepository {
    Optional<LegalEsnna> findSingleton();
    LegalEsnna save(LegalEsnna page);
}
