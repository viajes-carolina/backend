package com.viajescarolina.api.media.application.usecase;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.application.dto.UpdateMediaFocalPointRequest;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateMediaFocalPointUseCase {

    private final MediaRepository mediaRepository;

    @Inject
    public UpdateMediaFocalPointUseCase(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "UPDATE_MEDIA_FOCAL_POINT", entityType = "MEDIA_ASSET")
    @Transactional
    public MediaAssetDTO execute(Long id, UpdateMediaFocalPointRequest request) {
        MediaAsset asset = mediaRepository.findMediaById(id)
                .orElseThrow(() -> new NotFoundException("Recurso multimedia no encontrado con ID: " + id));

        asset.updateFocalPoint(request.focalX(), request.focalY());
        if (request.altText() != null || request.caption() != null) {
            asset.updateMetadata(
                    request.altText() != null ? request.altText() : asset.getAltText(),
                    request.caption() != null ? request.caption() : asset.getCaption()
            );
        }

        MediaAsset saved = mediaRepository.save(asset);
        return UploadMediaAssetUseCase.mapToDTO(saved);
    }
}
