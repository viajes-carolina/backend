package com.viajescarolina.api.home.domain;

import java.util.Optional;

public interface HomeFaqSectionRepository {
    Optional<HomeFaqSection> get();
    HomeFaqSection save(HomeFaqSection section);
}
