package com.viajescarolina.api.home.domain;

import java.util.Optional;

public interface HomeBlogInspirationRepository {
    Optional<HomeBlogInspiration> get();
    HomeBlogInspiration save(HomeBlogInspiration inspiration);
}
