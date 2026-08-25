package com.viajescarolina.api.claims.application.dto;

import com.viajescarolina.api.claims.domain.ClaimAttachment;
import java.time.OffsetDateTime;

public record ClaimAttachmentDTO(
    Long id,
    Long claimId,
    String originalFilename,
    String mimeType,
    long fileSizeBytes,
    OffsetDateTime createdAt
) {
    public static ClaimAttachmentDTO fromDomain(ClaimAttachment entity) {
        return new ClaimAttachmentDTO(
            entity.getId(),
            entity.getClaimId(),
            entity.getOriginalFilename(),
            entity.getMimeType(),
            entity.getFileSizeBytes(),
            entity.getCreatedAt()
        );
    }
}
