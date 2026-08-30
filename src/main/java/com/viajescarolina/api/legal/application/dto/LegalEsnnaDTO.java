package com.viajescarolina.api.legal.application.dto;

import java.time.Instant;
import java.util.List;

public record LegalEsnnaDTO(
    Long id,
    String eyebrow,
    String title,
    String introduction,
    String documentControlLabel,
    String documentControlText,
    String declarationEyebrow,
    String declarationTitle,
    String declarationBody,
    List<LegalSectionDTO> sections,
    String closingTitle,
    String closingBody,
    String closingLinkLabel,
    int revision,
    Instant updatedAt
) {}
