package com.viajescarolina.api.media.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.domain.MediaStorageService;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteMediaAssetUseCase {

    private final MediaRepository mediaRepository;
    private final MediaStorageService storageService;

    @Inject
    public DeleteMediaAssetUseCase(MediaRepository mediaRepository, MediaStorageService storageService) {
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
    }

    @Audited(action = "DELETE_MEDIA_ASSET", entityType = "MEDIA_ASSET")
    @Transactional
    public void execute(Long id) {
        MediaAsset asset = mediaRepository.findMediaById(id)
                .orElseThrow(() -> new NotFoundException("Recurso multimedia no encontrado con ID: " + id));

        asset.deactivate();
        mediaRepository.save(asset);
        storageService.delete(asset.getFilename());
    }
}
