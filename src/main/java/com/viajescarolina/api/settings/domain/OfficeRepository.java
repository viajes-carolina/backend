package com.viajescarolina.api.settings.domain;

import java.util.Optional;

public interface OfficeRepository {
    Optional<OfficeLocation> findOffice();
    OfficeLocation save(OfficeLocation office);
}
