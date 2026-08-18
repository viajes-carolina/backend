package com.viajescarolina.api.settings.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSiteSettingsRequest(
    @NotBlank(message = "El nombre del sitio es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    String siteName,

    @Size(max = 200, message = "El tagline no puede exceder 200 caracteres")
    String brandTagline,

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "Formato de correo inválido")
    String contactEmail,

    @NotBlank(message = "El teléfono principal es obligatorio")
    String primaryPhone,

    Integer logoMediaId,
    Integer faviconMediaId,
    String facebookUrl,
    String instagramUrl,
    String tiktokUrl
) {}
