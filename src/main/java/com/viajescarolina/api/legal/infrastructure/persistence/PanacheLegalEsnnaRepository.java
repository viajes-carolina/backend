package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalEsnna;
import com.viajescarolina.api.legal.domain.LegalEsnnaRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheLegalEsnnaRepository implements PanacheRepositoryBase<LegalEsnnaPanacheEntity, Long>, LegalEsnnaRepository {

    @Override
    public Optional<LegalEsnna> findSingleton() {
        return findByIdOptional(1L).map(LegalEsnnaPanacheEntity::toDomain);
    }

    @Override
    public LegalEsnna save(LegalEsnna page) {
        LegalEsnnaPanacheEntity entity = findById(1L);
        if (entity == null) {
            entity = LegalEsnnaPanacheEntity.fromDomain(page);
            persist(entity);
        } else {
            entity.copyFrom(page);
        }
        return entity.toDomain();
    }
}
