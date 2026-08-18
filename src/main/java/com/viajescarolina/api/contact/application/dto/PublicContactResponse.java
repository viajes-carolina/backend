package com.viajescarolina.api.contact.application.dto;

public record PublicContactResponse(
    ContactPageDTO page,
    String primaryPhone,
    String whatsappPhone,
    String contactEmail,
    String officeAddress,
    String officeHours
) {}
