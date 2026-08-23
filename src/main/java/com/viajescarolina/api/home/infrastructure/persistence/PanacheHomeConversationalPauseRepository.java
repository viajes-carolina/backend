package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeConversationalPause;
import com.viajescarolina.api.home.domain.HomeConversationalPauseRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomeConversationalPauseRepository implements HomeConversationalPauseRepository, PanacheRepositoryBase<HomeConversationalPausePanacheEntity, Long> {

    @Override
    public Optional<HomeConversationalPause> get() {
        HomeConversationalPausePanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(HomeConversationalPausePanacheEntity::toDomain);
    }

    @Override
    public HomeConversationalPause save(HomeConversationalPause pause) {
        HomeConversationalPausePanacheEntity entity = HomeConversationalPausePanacheEntity.fromDomain(pause);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
