package com.viajescarolina.api.media.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MediaAssetDTO(
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
        OffsetDateTime updatedAt
) {}
