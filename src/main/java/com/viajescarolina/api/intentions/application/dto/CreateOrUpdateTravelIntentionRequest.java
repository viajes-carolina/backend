package com.viajescarolina.api.intentions.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrUpdateTravelIntentionRequest(
        @NotBlank(message = "El slug es obligatorio")
        String slug,

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "El tagline es obligatorio")
        String tagline,

        String iconName,

        @NotNull(message = "Los destinos sugeridos son obligatorios")
        List<String> featuredDestinations,

        @NotBlank(message = "La plantilla de mensaje WhatsApp es obligatoria")
        String whatsappMessageTemplate,

        Long coverMediaId,
        Integer displayOrder,
        Boolean active
) {}
