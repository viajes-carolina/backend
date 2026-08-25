package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ClaimAttachmentDTO;
import com.viajescarolina.api.claims.domain.ClaimAttachment;
import com.viajescarolina.api.claims.domain.ClaimAttachmentRepository;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import com.viajescarolina.api.media.domain.MediaStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Set;

@ApplicationScoped
public class UploadClaimAttachmentUseCase {

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("application/pdf", "image/png", "image/jpeg");

    private final ClaimRepository claimRepository;
    private final ClaimAttachmentRepository attachmentRepository;
    private final MediaStorageService storageService;

    @ConfigProperty(name = "viajescarolina.claims.attachment-max-file-size", defaultValue = "10485760")
    long maxFileSizeBytes;

    @Inject
    public UploadClaimAttachmentUseCase(ClaimRepository claimRepository,
                                         ClaimAttachmentRepository attachmentRepository,
                                         MediaStorageService storageService) {
        this.claimRepository = claimRepository;
        this.attachmentRepository = attachmentRepository;
        this.storageService = storageService;
    }

    @Transactional
    public ClaimAttachmentDTO execute(Long claimId, String originalFilename, String mimeType,
                                       InputStream inputStream, long sizeBytes) {
        claimRepository.findClaimById(claimId)
                .orElseThrow(() -> new WebApplicationException(
                        "Reclamo no encontrado con ID: " + claimId, Response.Status.NOT_FOUND));

        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase(Locale.ROOT))) {
            throw new WebApplicationException(
                    "Tipo de archivo no permitido. Solo se aceptan PDF, PNG o JPEG.",
                    Response.Status.BAD_REQUEST);
        }
        if (sizeBytes > maxFileSizeBytes) {
            throw new WebApplicationException(
                    "El archivo excede el tamaño máximo permitido (" + (maxFileSizeBytes / 1024 / 1024) + " MB).",
                    Response.Status.BAD_REQUEST);
        }

        // storeRaw (no store): un adjunto puede ser un PDF, que ImageOptimizer rechazaría por
        // no decodificar como imagen — ver MediaStorageService.storeRaw.
        MediaStorageService.StoredFileInfo stored = storageService.storeRaw(originalFilename, mimeType, inputStream, sizeBytes);

        ClaimAttachment attachment = new ClaimAttachment(
                null,
                claimId,
                originalFilename,
                mimeType,
                stored.fileSizeBytes(),
                stored.filename(),
                OffsetDateTime.now()
        );

        ClaimAttachment saved = attachmentRepository.save(attachment);
        return ClaimAttachmentDTO.fromDomain(saved);
    }
}
