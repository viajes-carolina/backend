package com.viajescarolina.api.settings.application.dto;

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
    String minceturCertificateUrl,
    String minceturRegistrationNumber,
    String minceturLocation
) {}
