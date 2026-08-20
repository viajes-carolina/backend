package com.viajescarolina.api.settings.application.dto;

import java.time.Instant;

public record SiteSettingsDTO(
    Integer id,
    String siteName,
    String brandTagline,
    String contactEmail,
    Integer logoMediaId,
    Integer faviconMediaId,
    String facebookUrl,
    String instagramUrl,
    String tiktokUrl,
    String legalCompanyName,
    String taxId,
    // El teléfono real (llamadas y WhatsApp) vive únicamente en whatsapp_channel;
    // se compone aquí de solo lectura para que el panel admin edite todo desde un
    // solo formulario. Para editarlo, usar el use case de WhatsApp channel.
    String primaryPhone,
    String whatsappPhone,
    String whatsappDisplayNumber,
    String whatsappDefaultMessage,
    int revision,
    Instant updatedAt
) {}
