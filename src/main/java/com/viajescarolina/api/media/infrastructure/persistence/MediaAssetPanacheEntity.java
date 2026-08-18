package com.viajescarolina.api.media.infrastructure.persistence;

import com.viajescarolina.api.media.domain.MediaAsset;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "media_asset")
public class MediaAssetPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String filename;

    @Column(name = "original_name", nullable = false)
    public String originalName;

    @Column(name = "mime_type", nullable = false)
    public String mimeType;

    @Column(name = "file_size_bytes", nullable = false)
    public Long fileSizeBytes;

    @Column(nullable = false)
    public Integer width;

    @Column(nullable = false)
    public Integer height;

    @Column(name = "focal_x", nullable = false, precision = 5, scale = 2)
    public BigDecimal focalX;

    @Column(name = "focal_y", nullable = false, precision = 5, scale = 2)
    public BigDecimal focalY;

    @Column(name = "alt_text")
    public String altText;

    @Column
    public String caption;

    @Column(name = "storage_path", nullable = false)
    public String storagePath;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "variants_json", columnDefinition = "jsonb")
    public String variantsJson;

    @Column(name = "is_active", nullable = false)
    public Boolean isActive;

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public OffsetDateTime updatedAt;

    public MediaAsset toDomain() {
        return new MediaAsset(
                id,
                filename,
                originalName,
                mimeType,
                fileSizeBytes != null ? fileSizeBytes : 0L,
                width != null ? width : 0,
                height != null ? height : 0,
                focalX,
                focalY,
                altText,
                caption,
                storagePath,
                variantsJson,
                isActive != null ? isActive : true,
                createdAt,
                updatedAt
        );
    }

    public static MediaAssetPanacheEntity fromDomain(MediaAsset domain) {
        MediaAssetPanacheEntity entity = new MediaAssetPanacheEntity();
        entity.id = domain.getId();
        entity.filename = domain.getFilename();
        entity.originalName = domain.getOriginalName();
        entity.mimeType = domain.getMimeType();
        entity.fileSizeBytes = domain.getFileSizeBytes();
        entity.width = domain.getWidth();
        entity.height = domain.getHeight();
        entity.focalX = domain.getFocalX();
        entity.focalY = domain.getFocalY();
        entity.altText = domain.getAltText();
        entity.caption = domain.getCaption();
        entity.storagePath = domain.getStoragePath();
        entity.variantsJson = domain.getVariantsJson();
        entity.isActive = domain.isActive();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : OffsetDateTime.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : OffsetDateTime.now();
        return entity;
    }
}
