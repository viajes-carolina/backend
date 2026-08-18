package com.viajescarolina.api.trust.infrastructure.persistence;

import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheTestimonialRepository implements TestimonialRepository, PanacheRepositoryBase<TestimonialPanacheEntity, Long> {

    @Override
    public List<Testimonial> findAllActive() {
        return find("active = true ORDER BY displayOrder ASC").stream()
                .map(TestimonialPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<Testimonial> findAllTestimonials() {
        return list("ORDER BY displayOrder ASC").stream()
                .map(TestimonialPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Testimonial> findTestimonialById(Long id) {
        return findByIdOptional(id).map(TestimonialPanacheEntity::toDomain);
    }

    @Override
    public Testimonial save(Testimonial testimonial) {
        TestimonialPanacheEntity entity = TestimonialPanacheEntity.fromDomain(testimonial);
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
