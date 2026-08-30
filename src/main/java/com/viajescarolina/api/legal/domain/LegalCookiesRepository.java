package com.viajescarolina.api.legal.domain;

import java.util.Optional;

public interface LegalCookiesRepository {
    Optional<LegalCookies> findSingleton();
    LegalCookies save(LegalCookies page);
}
