package com.viajescarolina.api.legal.domain;

import java.util.Optional;

public interface LegalMinceturRepository {
    Optional<LegalMincetur> findSingleton();
    LegalMincetur save(LegalMincetur page);
}
