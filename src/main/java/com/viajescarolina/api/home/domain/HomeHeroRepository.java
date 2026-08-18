package com.viajescarolina.api.home.domain;

import java.util.Optional;

public interface HomeHeroRepository {

    Optional<HomeHero> findHero();

    HomeHero save(HomeHero homeHero);
}
