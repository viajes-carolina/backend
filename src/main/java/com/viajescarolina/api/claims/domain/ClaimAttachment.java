package com.viajescarolina.api.claims.domain;

import java.time.OffsetDateTime;

public class ClaimAttachment {
    private Long id;
    private Long claimId;
    private String originalFilename;
    private String mimeType;
    private long fileSizeBytes;
    private String storagePath;
    private OffsetDateTime createdAt;

    public ClaimAttachment() {}

    public ClaimAttachment(Long id, Long claimId, String originalFilename, String mimeType,
                            long fileSizeBytes, String storagePath, OffsetDateTime createdAt) {
        this.id = id;
        this.claimId = claimId;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.storagePath = storagePath;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    // Clave de recuperación en el backend de almacenamiento activo (MediaStorageService):
    // el "filename" único generado por store()/storeRaw(), no una URL pública. Se persiste
    // aquí (columna storage_path, ver V44) porque claim_attachment no distingue filename vs.
    // storage_path como sí lo hace media_asset — ver ClaimAttachmentPanacheEntity.
    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
