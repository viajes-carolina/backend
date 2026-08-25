package com.viajescarolina.api.contact.application.dto;

import java.math.BigDecimal;

public record PublicContactResponse(
    ContactPageDTO page,
    String primaryPhone,
    String whatsappPhone,
    String contactEmail,
    String officeAddress,
    String officeHours,
    String officeGoogleMapsUrl,
    BigDecimal officeLatitude,
    BigDecimal officeLongitude
) {}
