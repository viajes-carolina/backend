package com.viajescarolina.api.legal.application.dto;

import java.time.Instant;
import java.util.List;

public record LegalCookiesDTO(
    Long id,
    String eyebrow,
    String title,
    String introduction,
    String documentControlLabel,
    String documentControlText,
    List<LegalSectionDTO> sections,
    String closingTitle,
    String closingBody,
    String closingLinkLabel,
    List<CookieCategoryDTO> cookieCategories,
    String acceptAllLabel,
    String savePreferencesLabel,
    int revision,
    Instant updatedAt
) {}
