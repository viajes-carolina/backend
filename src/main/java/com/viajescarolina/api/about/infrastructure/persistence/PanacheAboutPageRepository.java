package com.viajescarolina.api.about.infrastructure.persistence;

import com.viajescarolina.api.about.domain.AboutPage;
import com.viajescarolina.api.about.domain.AboutPageRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheAboutPageRepository implements PanacheRepositoryBase<AboutPagePanacheEntity, Integer>, AboutPageRepository {

    @Override
    public Optional<AboutPage> findSingleton() {
        return findByIdOptional(1).map(AboutPagePanacheEntity::toDomain);
    }

    @Override
    public AboutPage save(AboutPage aboutPage) {
        AboutPagePanacheEntity entity = findById(1);
        if (entity == null) {
            entity = AboutPagePanacheEntity.fromDomain(aboutPage);
            persist(entity);
        } else {
            entity.copyFrom(aboutPage);
        }
        return entity.toDomain();
    }
}
