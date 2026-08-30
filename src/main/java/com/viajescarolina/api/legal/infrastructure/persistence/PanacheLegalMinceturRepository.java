package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalMincetur;
import com.viajescarolina.api.legal.domain.LegalMinceturRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheLegalMinceturRepository implements PanacheRepositoryBase<LegalMinceturPanacheEntity, Long>, LegalMinceturRepository {

    @Override
    public Optional<LegalMincetur> findSingleton() {
        return findByIdOptional(1L).map(LegalMinceturPanacheEntity::toDomain);
    }

    @Override
    public LegalMincetur save(LegalMincetur page) {
        LegalMinceturPanacheEntity entity = findById(1L);
        if (entity == null) {
            entity = LegalMinceturPanacheEntity.fromDomain(page);
            persist(entity);
        } else {
            entity.copyFrom(page);
        }
        return entity.toDomain();
    }
}
