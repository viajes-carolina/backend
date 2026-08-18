package com.viajescarolina.api.contact.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SubmitContactInquiryRequest(
    @NotBlank String fullName,
    @NotBlank @Email String email,
    String phone,
    String destinationOfInterest,
    String travelDateApprox,
    Integer travelersCount,
    @NotBlank String message,
    String preferredContactChannel,
    String turnstileToken
) {}
