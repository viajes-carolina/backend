package com.viajescarolina.api.media.application.usecase;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.domain.MediaStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@ApplicationScoped
public class UploadMediaAssetUseCase {

    private final MediaRepository mediaRepository;
    private final MediaStorageService storageService;

    @Inject
    public UploadMediaAssetUseCase(MediaRepository mediaRepository, MediaStorageService storageService) {
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
    }

    @Transactional
    public MediaAssetDTO execute(String originalFilename, String mimeType, InputStream inputStream, long sizeBytes, String altText, String caption) {
        MediaStorageService.StoredFileInfo stored = storageService.store(originalFilename, mimeType, inputStream, sizeBytes);

        MediaAsset asset = new MediaAsset(
                null,
                stored.filename(),
                originalFilename,
                mimeType,
                stored.fileSizeBytes(),
                stored.width(),
                stored.height(),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(50.0),
                altText != null ? altText : originalFilename,
                caption,
                stored.storagePath(),
                stored.variantsJson(),
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        MediaAsset saved = mediaRepository.save(asset);
        return mapToDTO(saved);
    }

    public static MediaAssetDTO mapToDTO(MediaAsset asset) {
        return new MediaAssetDTO(
                asset.getId(),
                asset.getFilename(),
                asset.getOriginalName(),
                asset.getMimeType(),
                asset.getFileSizeBytes(),
                asset.getWidth(),
                asset.getHeight(),
                asset.getFocalX(),
                asset.getFocalY(),
                asset.getAltText(),
                asset.getCaption(),
                asset.getStoragePath(),
                asset.getVariantsJson(),
                asset.isActive(),
                asset.getCreatedAt(),
                asset.getUpdatedAt()
        );
    }
}
