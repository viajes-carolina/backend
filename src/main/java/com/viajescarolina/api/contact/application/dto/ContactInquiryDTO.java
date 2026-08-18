package com.viajescarolina.api.contact.application.dto;

import java.time.Instant;

public record ContactInquiryDTO(
    Long id,
    String fullName,
    String email,
    String phone,
    String destinationOfInterest,
    String travelDateApprox,
    int travelersCount,
    String message,
    String preferredContactChannel,
    String status,
    boolean turnstileVerified,
    Instant createdAt,
    Instant updatedAt
) {}
