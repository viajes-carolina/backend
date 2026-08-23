package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import com.viajescarolina.api.home.domain.HomeTestimonialsSectionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomeTestimonialsSectionRepository implements HomeTestimonialsSectionRepository, PanacheRepositoryBase<HomeTestimonialsSectionPanacheEntity, Long> {

    @Override
    public Optional<HomeTestimonialsSection> get() {
        HomeTestimonialsSectionPanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(HomeTestimonialsSectionPanacheEntity::toDomain);
    }

    @Override
    public HomeTestimonialsSection save(HomeTestimonialsSection section) {
        HomeTestimonialsSectionPanacheEntity entity = HomeTestimonialsSectionPanacheEntity.fromDomain(section);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
