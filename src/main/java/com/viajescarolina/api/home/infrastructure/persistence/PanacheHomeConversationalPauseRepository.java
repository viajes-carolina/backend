package com.viajescarolina.api.home.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.home.domain.HomeConversationalPause;
import com.viajescarolina.api.home.domain.HomeConversationalPauseRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomeConversationalPauseRepository implements HomeConversationalPauseRepository, PanacheRepositoryBase<HomeConversationalPausePanacheEntity, Long> {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Optional<HomeConversationalPause> get() {
        HomeConversationalPausePanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(e -> e.toDomain(objectMapper));
    }

    @Override
    public HomeConversationalPause save(HomeConversationalPause pause) {
        HomeConversationalPausePanacheEntity entity = HomeConversationalPausePanacheEntity.fromDomain(pause, objectMapper);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain(objectMapper);
    }
}
