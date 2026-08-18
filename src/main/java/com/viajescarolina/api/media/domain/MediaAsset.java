package com.viajescarolina.api.media.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class MediaAsset {

    private final Long id;
    private final String filename;
    private final String originalName;
    private final String mimeType;
    private final long fileSizeBytes;
    private final int width;
    private final int height;
    private BigDecimal focalX;
    private BigDecimal focalY;
    private String altText;
    private String caption;
    private final String storagePath;
    private String variantsJson;
    private boolean active;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public MediaAsset(
            Long id,
            String filename,
            String originalName,
            String mimeType,
            long fileSizeBytes,
            int width,
            int height,
            BigDecimal focalX,
            BigDecimal focalY,
            String altText,
            String caption,
            String storagePath,
            String variantsJson,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.filename = filename;
        this.originalName = originalName;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.width = width;
        this.height = height;
        this.focalX = focalX != null ? focalX : BigDecimal.valueOf(50.0);
        this.focalY = focalY != null ? focalY : BigDecimal.valueOf(50.0);
        this.altText = altText;
        this.caption = caption;
        this.storagePath = storagePath;
        this.variantsJson = variantsJson != null ? variantsJson : "{}";
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateFocalPoint(BigDecimal newFocalX, BigDecimal newFocalY) {
        if (newFocalX != null && newFocalX.compareTo(BigDecimal.ZERO) >= 0 && newFocalX.compareTo(BigDecimal.valueOf(100.0)) <= 0) {
            this.focalX = newFocalX;
        }
        if (newFocalY != null && newFocalY.compareTo(BigDecimal.ZERO) >= 0 && newFocalY.compareTo(BigDecimal.valueOf(100.0)) <= 0) {
            this.focalY = newFocalY;
        }
        this.updatedAt = OffsetDateTime.now();
    }

    public void updateMetadata(String altText, String caption) {
        this.altText = altText;
        this.caption = caption;
        this.updatedAt = OffsetDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = OffsetDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getOriginalName() { return originalName; }
    public String getMimeType() { return mimeType; }
    public long getFileSizeBytes() { return fileSizeBytes; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public BigDecimal getFocalX() { return focalX; }
    public BigDecimal getFocalY() { return focalY; }
    public String getAltText() { return altText; }
    public String getCaption() { return caption; }
    public String getStoragePath() { return storagePath; }
    public String getVariantsJson() { return variantsJson; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
