package com.viajescarolina.api.media.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MediaRepository {

    MediaAsset save(MediaAsset mediaAsset);

    Optional<MediaAsset> findMediaById(Long id);

    /**
     * Resuelve varios medios en una sola consulta batch (evita el patrón N+1
     * de invocar {@link #findMediaById(Long)} dentro de un loop).
     */
    List<MediaAsset> findMediaByIds(Collection<Long> ids);

    Optional<MediaAsset> findByFilename(String filename);

    List<MediaAsset> findAll(int page, int size, String mimeTypeFilter);

    long count(String mimeTypeFilter);

    void delete(Long id);
}
