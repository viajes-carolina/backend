package com.viajescarolina.api.home.domain;

import java.util.Optional;

public interface HomeConversationalPauseRepository {
    Optional<HomeConversationalPause> get();
    HomeConversationalPause save(HomeConversationalPause pause);
}
