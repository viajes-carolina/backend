package com.viajescarolina.api.media.application.usecase;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListMediaAssetsUseCase {

    private final MediaRepository mediaRepository;

    @Inject
    public ListMediaAssetsUseCase(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public record MediaPageResponse(List<MediaAssetDTO> items, long total, int page, int size) {}

    public MediaPageResponse execute(int page, int size, String mimeTypeFilter) {
        List<MediaAsset> assets = mediaRepository.findAll(page, size, mimeTypeFilter);
        long total = mediaRepository.count(mimeTypeFilter);
        List<MediaAssetDTO> dtos = assets.stream().map(UploadMediaAssetUseCase::mapToDTO).toList();
        return new MediaPageResponse(dtos, total, page, size);
    }
}
