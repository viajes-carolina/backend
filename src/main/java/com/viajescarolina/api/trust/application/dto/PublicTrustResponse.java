package com.viajescarolina.api.trust.application.dto;

import java.util.List;

public record PublicTrustResponse(
        List<TestimonialDTO> testimonials,
        List<FaqItemDTO> faqs
) {}
