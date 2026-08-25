package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.domain.ClaimAttachment;
import com.viajescarolina.api.claims.domain.ClaimAttachmentRepository;
import com.viajescarolina.api.media.domain.MediaStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.io.InputStream;

@ApplicationScoped
public class DownloadClaimAttachmentUseCase {

    public record AttachmentFile(InputStream content, String mimeType, String originalFilename) {}

    private final ClaimAttachmentRepository attachmentRepository;
    private final MediaStorageService storageService;

    @Inject
    public DownloadClaimAttachmentUseCase(ClaimAttachmentRepository attachmentRepository, MediaStorageService storageService) {
        this.attachmentRepository = attachmentRepository;
        this.storageService = storageService;
    }

    public AttachmentFile execute(Long claimId, Long attachmentId) {
        ClaimAttachment attachment = attachmentRepository.findAttachmentById(attachmentId)
                .filter(a -> a.getClaimId().equals(claimId))
                .orElseThrow(() -> new NotFoundException("Adjunto no encontrado con ID: " + attachmentId));

        InputStream content = storageService.retrieve(attachment.getStoragePath());
        return new AttachmentFile(content, attachment.getMimeType(), attachment.getOriginalFilename());
    }
}
