package com.viajescarolina.api.blog.infrastructure.persistence;

import com.viajescarolina.api.blog.domain.BlogHero;
import com.viajescarolina.api.blog.domain.BlogHeroRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheBlogHeroRepository implements BlogHeroRepository, PanacheRepositoryBase<BlogHeroPanacheEntity, Long> {

    @Override
    public Optional<BlogHero> get() {
        BlogHeroPanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(BlogHeroPanacheEntity::toDomain);
    }

    @Override
    public BlogHero save(BlogHero hero) {
        BlogHeroPanacheEntity entity = BlogHeroPanacheEntity.fromDomain(hero);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
