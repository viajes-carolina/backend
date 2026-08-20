package com.viajescarolina.api.media.application.usecase;

import com.viajescarolina.api.media.application.dto.MediaAssetDTO;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.domain.MediaStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Set;

@ApplicationScoped
public class UploadMediaAssetUseCase {

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/png", "image/jpeg", "image/webp", "image/gif");

    private final MediaRepository mediaRepository;
    private final MediaStorageService storageService;

    @ConfigProperty(name = "viajescarolina.media.max-file-size", defaultValue = "10485760")
    long maxFileSizeBytes;

    @Inject
    public UploadMediaAssetUseCase(MediaRepository mediaRepository, MediaStorageService storageService) {
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
    }

    @Transactional
    public MediaAssetDTO execute(String originalFilename, String mimeType, InputStream inputStream, long sizeBytes, String altText, String caption) {
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase(Locale.ROOT))) {
            throw new WebApplicationException(
                    "Tipo de archivo no permitido. Solo se aceptan imágenes PNG, JPEG, WEBP o GIF.",
                    Response.Status.BAD_REQUEST);
        }
        if (sizeBytes > maxFileSizeBytes) {
            throw new WebApplicationException(
                    "El archivo excede el tamaño máximo permitido (" + (maxFileSizeBytes / 1024 / 1024) + " MB).",
                    Response.Status.BAD_REQUEST);
        }

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
