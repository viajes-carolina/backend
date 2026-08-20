package com.viajescarolina.api.settings.application.dto;

import java.util.Map;

public record PublicSiteResponse(
    String siteName,
    String brandTagline,
    String contactEmail,
    String primaryPhone,
    String whatsappPhone,
    String whatsappDisplayNumber,
    String facebookUrl,
    String instagramUrl,
    String tiktokUrl,
    Integer logoMediaId,
    Integer faviconMediaId,
    String legalCompanyName,
    String taxId,
    Map<String, String> actions
) {}
