package com.viajescarolina.api.blog.infrastructure.persistence;

import com.viajescarolina.api.blog.domain.BlogLibrary;
import com.viajescarolina.api.blog.domain.BlogLibraryRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PanacheBlogLibraryRepository implements BlogLibraryRepository, PanacheRepositoryBase<BlogLibraryPanacheEntity, Long> {

    @Override
    public Optional<BlogLibrary> get() {
        BlogLibraryPanacheEntity entity = findById(1L);
        return Optional.ofNullable(entity).map(BlogLibraryPanacheEntity::toDomain);
    }

    @Override
    public BlogLibrary save(BlogLibrary library) {
        BlogLibraryPanacheEntity entity = BlogLibraryPanacheEntity.fromDomain(library);
        entity.updatedAt = Instant.now();
        entity = getEntityManager().merge(entity);
        return entity.toDomain();
    }
}
