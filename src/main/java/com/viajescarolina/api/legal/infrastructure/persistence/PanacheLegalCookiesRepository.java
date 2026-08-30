package com.viajescarolina.api.legal.infrastructure.persistence;

import com.viajescarolina.api.legal.domain.LegalCookies;
import com.viajescarolina.api.legal.domain.LegalCookiesRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheLegalCookiesRepository implements PanacheRepositoryBase<LegalCookiesPanacheEntity, Long>, LegalCookiesRepository {

    @Override
    public Optional<LegalCookies> findSingleton() {
        return findByIdOptional(1L).map(LegalCookiesPanacheEntity::toDomain);
    }

    @Override
    public LegalCookies save(LegalCookies page) {
        LegalCookiesPanacheEntity entity = findById(1L);
        if (entity == null) {
            entity = LegalCookiesPanacheEntity.fromDomain(page);
            persist(entity);
        } else {
            entity.copyFrom(page);
        }
        return entity.toDomain();
    }
}
