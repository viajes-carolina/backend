package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalMinceturDTO;
import com.viajescarolina.api.legal.domain.LegalMincetur;
import com.viajescarolina.api.legal.domain.LegalMinceturRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetPublicLegalMinceturUseCase {

    private final LegalMinceturRepository repository;

    public GetPublicLegalMinceturUseCase(LegalMinceturRepository repository) {
        this.repository = repository;
    }

    public LegalMinceturDTO execute() {
        LegalMincetur page = repository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de Constancia MINCETUR no inicializados"));
        return toDTO(page);
    }

    public LegalMinceturDTO toDTO(LegalMincetur p) {
        return new LegalMinceturDTO(
            p.getId(),
            p.getEyebrow(),
            p.getTitle(),
            p.getIntroduction(),
            p.getDocumentControlLabel(),
            p.getDocumentControlText(),
            GetPublicLegalTermsUseCase.toSectionDTOs(p.getSections()),
            p.getVerificationEyebrow(),
            p.getVerificationButtonLabel(),
            p.getVerificationNote(),
            p.getClosingTitle(),
            p.getClosingBody(),
            p.getClosingLinkLabel(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }
}
