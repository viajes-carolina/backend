package com.viajescarolina.api.contact.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateInquiryStatusRequest(
    @NotBlank String status
) {}
