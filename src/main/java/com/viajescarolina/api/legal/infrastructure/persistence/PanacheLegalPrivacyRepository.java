package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalPrivacy;
import com.viajescarolina.api.legal.domain.LegalPrivacyRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheLegalPrivacyRepository implements PanacheRepositoryBase<LegalPrivacyPanacheEntity, Long>, LegalPrivacyRepository {

    @Override
    public Optional<LegalPrivacy> findSingleton() {
        return findByIdOptional(1L).map(LegalPrivacyPanacheEntity::toDomain);
    }

    @Override
    public LegalPrivacy save(LegalPrivacy page) {
        LegalPrivacyPanacheEntity entity = findById(1L);
        if (entity == null) {
            entity = LegalPrivacyPanacheEntity.fromDomain(page);
            persist(entity);
        } else {
            entity.copyFrom(page);
        }
        return entity.toDomain();
    }
}
