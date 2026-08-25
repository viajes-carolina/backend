package com.viajescarolina.api.promotions.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payload del formulario estructurado con el que el admin crea una promoción. Sin
 * {@code slug} (se autogenera desde el título) y sin campos de origen/estado de Facebook
 * ({@code source}, {@code active} siempre nace {@code true}) — esos los fija el dominio.
 */
public record CreateOrUpdatePromotionRequest(
        @NotBlank String title,
        @NotBlank String destination,
        @NotBlank String summary,
        @NotNull BigDecimal priceUsd,
        BigDecimal pricePen,
        @NotNull Integer durationDays,
        @NotNull Integer durationNights,
        String departureCity,
        LocalDate validFrom,
        LocalDate validUntil,
        Long featuredMediaId,
        List<String> inclusions,
        List<String> exclusions,
        String whatsappMessageTemplate
) {}
