package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ClaimAttachment;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "claim_attachment")
public class ClaimAttachmentPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "claim_id", nullable = false)
    public Long claimId;

    @Column(name = "original_filename", nullable = false, length = 255)
    public String originalFilename;

    @Column(name = "mime_type", nullable = false, length = 100)
    public String mimeType;

    @Column(name = "file_size_bytes", nullable = false)
    public Long fileSizeBytes;

    // Clave de recuperación en MediaStorageService (filename único), no una URL pública —
    // ver comentario en ClaimAttachment.getStoragePath().
    @Column(name = "storage_path", nullable = false, length = 500)
    public String storagePath;

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime createdAt;

    public ClaimAttachment toDomain() {
        return new ClaimAttachment(
            id, claimId, originalFilename, mimeType,
            fileSizeBytes != null ? fileSizeBytes : 0L, storagePath, createdAt
        );
    }

    public static ClaimAttachmentPanacheEntity fromDomain(ClaimAttachment domain) {
        ClaimAttachmentPanacheEntity entity = new ClaimAttachmentPanacheEntity();
        entity.id = domain.getId();
        entity.claimId = domain.getClaimId();
        entity.originalFilename = domain.getOriginalFilename();
        entity.mimeType = domain.getMimeType();
        entity.fileSizeBytes = domain.getFileSizeBytes();
        entity.storagePath = domain.getStoragePath();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : OffsetDateTime.now();
        return entity;
    }
}
