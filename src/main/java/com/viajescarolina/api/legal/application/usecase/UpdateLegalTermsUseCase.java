package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalSectionDTO;
import com.viajescarolina.api.legal.application.dto.LegalTermsDTO;
import com.viajescarolina.api.legal.application.dto.UpdateLegalTermsRequest;
import com.viajescarolina.api.legal.domain.LegalSection;
import com.viajescarolina.api.legal.domain.LegalTerms;
import com.viajescarolina.api.legal.domain.LegalTermsRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateLegalTermsUseCase {

    private final LegalTermsRepository repository;
    private final GetPublicLegalTermsUseCase getPublicLegalTermsUseCase;

    public UpdateLegalTermsUseCase(LegalTermsRepository repository, GetPublicLegalTermsUseCase getPublicLegalTermsUseCase) {
        this.repository = repository;
        this.getPublicLegalTermsUseCase = getPublicLegalTermsUseCase;
    }

    @Audited(action = "UPDATE_LEGAL_TERMS", entityType = "LEGAL_TERMS")
    @Transactional
    public LegalTermsDTO execute(UpdateLegalTermsRequest req) {
        LegalTerms page = repository.findSingleton()
            .orElseGet(() -> {
                LegalTerms newP = new LegalTerms();
                newP.setId(1L);
                newP.setRevision(1);
                newP.setCreatedAt(Instant.now());
                return newP;
            });

        page.setEyebrow(req.eyebrow());
        page.setTitle(req.title());
        page.setIntroduction(req.introduction());
        page.setDocumentControlLabel(req.documentControlLabel());
        page.setDocumentControlText(req.documentControlText());
        page.setSections(toSections(req.sections()));
        page.setClosingTitle(req.closingTitle());
        page.setClosingBody(req.closingBody());
        page.setClosingLinkLabel(req.closingLinkLabel());

        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        LegalTerms saved = repository.save(page);
        return getPublicLegalTermsUseCase.toDTO(saved);
    }

    private static List<LegalSection> toSections(List<LegalSectionDTO> sections) {
        if (sections == null) return List.of();
        return sections.stream().map(s -> new LegalSection(s.title(), s.body())).toList();
    }
}
