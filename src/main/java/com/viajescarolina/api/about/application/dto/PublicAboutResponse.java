package com.viajescarolina.api.about.application.dto;

import java.util.List;

public record PublicAboutResponse(
    AboutPageDTO page,
    List<TravelAdvisorDTO> advisors
) {}
