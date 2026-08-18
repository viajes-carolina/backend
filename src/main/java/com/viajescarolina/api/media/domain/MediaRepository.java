package com.viajescarolina.api.media.domain;

import java.util.List;
import java.util.Optional;

public interface MediaRepository {

    MediaAsset save(MediaAsset mediaAsset);

    Optional<MediaAsset> findMediaById(Long id);

    Optional<MediaAsset> findByFilename(String filename);

    List<MediaAsset> findAll(int page, int size, String mimeTypeFilter);

    long count(String mimeTypeFilter);

    void delete(Long id);
}
