package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeHero;
import com.viajescarolina.api.home.domain.HomeHeroRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheHomeHeroRepository implements HomeHeroRepository, PanacheRepositoryBase<HomeHeroPanacheEntity, Integer> {

    @Override
    public Optional<HomeHero> findHero() {
        HomeHeroPanacheEntity entity = findById(1);
        return Optional.ofNullable(entity).map(HomeHeroPanacheEntity::toDomain);
    }

    @Override
    public HomeHero save(HomeHero homeHero) {
        HomeHeroPanacheEntity entity = HomeHeroPanacheEntity.fromDomain(homeHero);
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
