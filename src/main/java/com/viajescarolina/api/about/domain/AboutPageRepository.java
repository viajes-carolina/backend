package com.viajescarolina.api.about.domain;

import java.util.Optional;

public interface AboutPageRepository {
    Optional<AboutPage> findSingleton();
    AboutPage save(AboutPage aboutPage);
}
