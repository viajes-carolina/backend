package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalPrivacyDTO;
import com.viajescarolina.api.legal.domain.LegalPrivacy;
import com.viajescarolina.api.legal.domain.LegalPrivacyRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetPublicLegalPrivacyUseCase {

    private final LegalPrivacyRepository repository;

    public GetPublicLegalPrivacyUseCase(LegalPrivacyRepository repository) {
        this.repository = repository;
    }

    public LegalPrivacyDTO execute() {
        LegalPrivacy page = repository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de Política de privacidad no inicializados"));
        return toDTO(page);
    }

    public LegalPrivacyDTO toDTO(LegalPrivacy p) {
        return new LegalPrivacyDTO(
            p.getId(),
            p.getEyebrow(),
            p.getTitle(),
            p.getIntroduction(),
            p.getDocumentControlLabel(),
            p.getDocumentControlText(),
            GetPublicLegalTermsUseCase.toSectionDTOs(p.getSections()),
            p.getClosingTitle(),
            p.getClosingBody(),
            p.getClosingLinkLabel(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }
}
