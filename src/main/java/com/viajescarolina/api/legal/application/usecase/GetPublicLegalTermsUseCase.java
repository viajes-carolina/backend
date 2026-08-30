package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalSectionDTO;
import com.viajescarolina.api.legal.application.dto.LegalTermsDTO;
import com.viajescarolina.api.legal.domain.LegalSection;
import com.viajescarolina.api.legal.domain.LegalTerms;
import com.viajescarolina.api.legal.domain.LegalTermsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GetPublicLegalTermsUseCase {

    private final LegalTermsRepository repository;

    public GetPublicLegalTermsUseCase(LegalTermsRepository repository) {
        this.repository = repository;
    }

    public LegalTermsDTO execute() {
        LegalTerms page = repository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de Términos y condiciones no inicializados"));
        return toDTO(page);
    }

    public LegalTermsDTO toDTO(LegalTerms p) {
        return new LegalTermsDTO(
            p.getId(),
            p.getEyebrow(),
            p.getTitle(),
            p.getIntroduction(),
            p.getDocumentControlLabel(),
            p.getDocumentControlText(),
            toSectionDTOs(p.getSections()),
            p.getClosingTitle(),
            p.getClosingBody(),
            p.getClosingLinkLabel(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }

    static List<LegalSectionDTO> toSectionDTOs(List<LegalSection> sections) {
        if (sections == null) return List.of();
        return sections.stream().map(s -> new LegalSectionDTO(s.title(), s.body())).toList();
    }
}
