package com.viajescarolina.api.claims.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrUpdateContactExploreLinkRequest(
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    String title,

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255)
    String description,

    @NotBlank(message = "El icono es obligatorio")
    @Size(max = 50)
    String iconName,

    @NotBlank(message = "La URL de destino es obligatoria")
    @Size(max = 255)
    String targetUrl,

    @NotBlank(message = "El texto del botón es obligatorio")
    @Size(max = 60)
    String buttonText,

    Integer displayOrder,
    Boolean active
) {}
