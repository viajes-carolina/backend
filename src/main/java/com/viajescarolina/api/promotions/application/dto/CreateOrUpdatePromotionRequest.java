package com.viajescarolina.api.promotions.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateOrUpdatePromotionRequest(
        @NotBlank(message = "El slug es obligatorio")
        String slug,

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "El destino es obligatorio")
        String destination,

        @NotBlank(message = "El resumen es obligatorio")
        String summary,

        @NotNull(message = "El precio en USD es obligatorio")
        BigDecimal priceUsd,

        BigDecimal pricePen,

        @NotNull(message = "Los días de duración son obligatorios")
        Integer durationDays,

        @NotNull(message = "Las noches de duración son obligatorias")
        Integer durationNights,

        String departureCity,
        LocalDate validFrom,
        LocalDate validUntil,
        Long featuredMediaId,
        Boolean isFeatured,
        List<String> inclusions,
        List<String> exclusions,
        String whatsappMessageTemplate,
        Integer displayOrder,
        Boolean active
) {}
