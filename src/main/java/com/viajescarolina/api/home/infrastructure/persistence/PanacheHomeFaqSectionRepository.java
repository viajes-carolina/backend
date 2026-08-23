package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeFaqSection;
import com.viajescarolina.api.home.domain.HomeFaqSectionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomeFaqSectionRepository implements HomeFaqSectionRepository, PanacheRepositoryBase<HomeFaqSectionPanacheEntity, Long> {

    @Override
    public Optional<HomeFaqSection> get() {
        HomeFaqSectionPanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(HomeFaqSectionPanacheEntity::toDomain);
    }

    @Override
    public HomeFaqSection save(HomeFaqSection section) {
        HomeFaqSectionPanacheEntity entity = HomeFaqSectionPanacheEntity.fromDomain(section);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
