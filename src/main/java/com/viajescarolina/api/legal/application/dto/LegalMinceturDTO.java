package com.viajescarolina.api.legal.application.dto;

import java.time.Instant;
import java.util.List;

public record LegalMinceturDTO(
    Long id,
    String eyebrow,
    String title,
    String introduction,
    String documentControlLabel,
    String documentControlText,
    List<LegalSectionDTO> sections,
    String verificationEyebrow,
    String verificationButtonLabel,
    String verificationNote,
    String closingTitle,
    String closingBody,
    String closingLinkLabel,
    int revision,
    Instant updatedAt
) {}
