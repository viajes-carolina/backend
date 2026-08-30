package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalTerms;
import com.viajescarolina.api.legal.domain.LegalTermsRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheLegalTermsRepository implements PanacheRepositoryBase<LegalTermsPanacheEntity, Long>, LegalTermsRepository {

    @Override
    public Optional<LegalTerms> findSingleton() {
        return findByIdOptional(1L).map(LegalTermsPanacheEntity::toDomain);
    }

    @Override
    public LegalTerms save(LegalTerms page) {
        LegalTermsPanacheEntity entity = findById(1L);
        if (entity == null) {
            entity = LegalTermsPanacheEntity.fromDomain(page);
            persist(entity);
        } else {
            entity.copyFrom(page);
        }
        return entity.toDomain();
    }
}
