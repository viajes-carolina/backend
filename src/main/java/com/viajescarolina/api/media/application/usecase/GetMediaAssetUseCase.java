package com.viajescarolina.api.media.application.usecase;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class GetMediaAssetUseCase {

    private final MediaRepository mediaRepository;

    @Inject
    public GetMediaAssetUseCase(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public MediaAssetDTO execute(Long id) {
        MediaAsset asset = mediaRepository.findMediaById(id)
                .orElseThrow(() -> new NotFoundException("Recurso multimedia no encontrado con ID: " + id));
        return UploadMediaAssetUseCase.mapToDTO(asset);
    }

    public MediaAssetDTO executeByFilename(String filename) {
        MediaAsset asset = mediaRepository.findByFilename(filename)
                .orElseThrow(() -> new NotFoundException("Recurso multimedia no encontrado con nombre: " + filename));
        return UploadMediaAssetUseCase.mapToDTO(asset);
    }
}
