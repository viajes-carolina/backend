package com.viajescarolina.api.intentions.infrastructure.persistence;

import com.viajescarolina.api.intentions.domain.TravelIntention;
import com.viajescarolina.api.intentions.domain.TravelIntentionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheTravelIntentionRepository implements TravelIntentionRepository, PanacheRepositoryBase<TravelIntentionPanacheEntity, Long> {

    @Override
    public List<TravelIntention> findAllActive() {
        return find("isActive = true ORDER BY displayOrder ASC").stream()
                .map(TravelIntentionPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<TravelIntention> findAllIntentions() {
        return list("ORDER BY displayOrder ASC").stream()
                .map(TravelIntentionPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<TravelIntention> findIntentionById(Long id) {
        return findByIdOptional(id).map(TravelIntentionPanacheEntity::toDomain);
    }

    @Override
    public Optional<TravelIntention> findBySlug(String slug) {
        return find("slug", slug).firstResultOptional().map(TravelIntentionPanacheEntity::toDomain);
    }

    @Override
    public TravelIntention save(TravelIntention travelIntention) {
        TravelIntentionPanacheEntity entity = TravelIntentionPanacheEntity.fromDomain(travelIntention);
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
