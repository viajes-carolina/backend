package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomePromotionsSection;
import com.viajescarolina.api.home.domain.HomePromotionsSectionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomePromotionsSectionRepository implements HomePromotionsSectionRepository, PanacheRepositoryBase<HomePromotionsSectionPanacheEntity, Long> {

    @Override
    public Optional<HomePromotionsSection> get() {
        HomePromotionsSectionPanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(HomePromotionsSectionPanacheEntity::toDomain);
    }

    @Override
    public HomePromotionsSection save(HomePromotionsSection section) {
        HomePromotionsSectionPanacheEntity entity = HomePromotionsSectionPanacheEntity.fromDomain(section);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
