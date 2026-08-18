package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeBlogInspiration;
import com.viajescarolina.api.home.domain.HomeBlogInspirationRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomeBlogInspirationRepository implements HomeBlogInspirationRepository, PanacheRepositoryBase<HomeBlogInspirationPanacheEntity, Long> {

    @Override
    public Optional<HomeBlogInspiration> get() {
        HomeBlogInspirationPanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(HomeBlogInspirationPanacheEntity::toDomain);
    }

    @Override
    public HomeBlogInspiration save(HomeBlogInspiration inspiration) {
        HomeBlogInspirationPanacheEntity entity = HomeBlogInspirationPanacheEntity.fromDomain(inspiration);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
