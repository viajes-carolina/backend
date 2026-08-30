package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalEsnnaDTO;
import com.viajescarolina.api.legal.domain.LegalEsnna;
import com.viajescarolina.api.legal.domain.LegalEsnnaRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetPublicLegalEsnnaUseCase {

    private final LegalEsnnaRepository repository;

    public GetPublicLegalEsnnaUseCase(LegalEsnnaRepository repository) {
        this.repository = repository;
    }

    public LegalEsnnaDTO execute() {
        LegalEsnna page = repository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de Compromiso contra la ESNNA no inicializados"));
        return toDTO(page);
    }

    public LegalEsnnaDTO toDTO(LegalEsnna p) {
        return new LegalEsnnaDTO(
            p.getId(),
            p.getEyebrow(),
            p.getTitle(),
            p.getIntroduction(),
            p.getDocumentControlLabel(),
            p.getDocumentControlText(),
            p.getDeclarationEyebrow(),
            p.getDeclarationTitle(),
            p.getDeclarationBody(),
            GetPublicLegalTermsUseCase.toSectionDTOs(p.getSections()),
            p.getClosingTitle(),
            p.getClosingBody(),
            p.getClosingLinkLabel(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }
}
