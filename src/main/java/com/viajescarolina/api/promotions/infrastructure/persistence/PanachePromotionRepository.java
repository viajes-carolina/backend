package com.viajescarolina.api.promotions.infrastructure.persistence;

import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanachePromotionRepository implements PromotionRepository, PanacheRepositoryBase<PromotionPanacheEntity, Long> {

    @Override
    public List<Promotion> findFeatured() {
        return find("isFeatured = true AND active = true ORDER BY displayOrder ASC").stream()
                .map(PromotionPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<Promotion> findAllActive() {
        return find("active = true ORDER BY displayOrder ASC").stream()
                .map(PromotionPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<Promotion> findAllPromotions() {
        return list("ORDER BY displayOrder ASC").stream()
                .map(PromotionPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Promotion> findPromotionById(Long id) {
        return findByIdOptional(id).map(PromotionPanacheEntity::toDomain);
    }

    @Override
    public Optional<Promotion> findBySlug(String slug) {
        return find("slug", slug).firstResultOptional().map(PromotionPanacheEntity::toDomain);
    }

    @Override
    public Promotion save(Promotion promotion) {
        PromotionPanacheEntity entity = PromotionPanacheEntity.fromDomain(promotion);
        if (entity.id == null) {
            persist(entity);
            return entity.toDomain();
        } else {
            entity = getEntityManager().merge(entity);
            return entity.toDomain();
        }
    }

    @Override
    public void delete(Long id) {
        deleteById(id);
    }
}
