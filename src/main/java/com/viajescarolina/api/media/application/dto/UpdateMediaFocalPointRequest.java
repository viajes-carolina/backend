package com.viajescarolina.api.media.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateMediaFocalPointRequest(
        @NotNull(message = "El punto focal X es requerido")
        @DecimalMin(value = "0.0", message = "El punto focal X debe ser >= 0")
        @DecimalMax(value = "100.0", message = "El punto focal X debe ser <= 100")
        BigDecimal focalX,

        @NotNull(message = "El punto focal Y es requerido")
        @DecimalMin(value = "0.0", message = "El punto focal Y debe ser >= 0")
        @DecimalMax(value = "100.0", message = "El punto focal Y debe ser <= 100")
        BigDecimal focalY,

        String altText,
        String caption
) {}
