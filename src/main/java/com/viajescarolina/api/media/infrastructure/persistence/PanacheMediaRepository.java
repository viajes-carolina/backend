package com.viajescarolina.api.media.infrastructure.persistence;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheMediaRepository implements MediaRepository, PanacheRepositoryBase<MediaAssetPanacheEntity, Long> {

    @Override
    public MediaAsset save(MediaAsset mediaAsset) {
        MediaAssetPanacheEntity entity = MediaAssetPanacheEntity.fromDomain(mediaAsset);
        if (entity.id == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return entity.toDomain();
    }

    @Override
    public Optional<MediaAsset> findMediaById(Long id) {
        MediaAssetPanacheEntity entity = find("id = ?1 and isActive = true", id).firstResult();
        return Optional.ofNullable(entity).map(MediaAssetPanacheEntity::toDomain);
    }

    @Override
    public Optional<MediaAsset> findByFilename(String filename) {
        MediaAssetPanacheEntity entity = find("filename = ?1 and isActive = true", filename).firstResult();
        return Optional.ofNullable(entity).map(MediaAssetPanacheEntity::toDomain);
    }

    @Override
    public List<MediaAsset> findAll(int page, int size, String mimeTypeFilter) {
        Sort sort = Sort.by("createdAt", Sort.Direction.Descending);
        Page p = Page.of(page, size);

        if (mimeTypeFilter != null && !mimeTypeFilter.isBlank()) {
            return find("isActive = true and mimeType like ?1", sort, "%" + mimeTypeFilter + "%")
                    .page(p)
                    .list()
                    .stream()
                    .map(MediaAssetPanacheEntity::toDomain)
                    .toList();
        }

        return find("isActive = true", sort)
                .page(p)
                .list()
                .stream()
                .map(MediaAssetPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public long count(String mimeTypeFilter) {
        if (mimeTypeFilter != null && !mimeTypeFilter.isBlank()) {
            return count("isActive = true and mimeType like ?1", "%" + mimeTypeFilter + "%");
        }
        return count("isActive = true");
    }

    @Override
    public void delete(Long id) {
        update("isActive = false where id = ?1", id);
    }
}
