package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalEsnnaDTO;
import com.viajescarolina.api.legal.application.dto.LegalSectionDTO;
import com.viajescarolina.api.legal.application.dto.UpdateLegalEsnnaRequest;
import com.viajescarolina.api.legal.domain.LegalEsnna;
import com.viajescarolina.api.legal.domain.LegalEsnnaRepository;
import com.viajescarolina.api.legal.domain.LegalSection;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateLegalEsnnaUseCase {

    private final LegalEsnnaRepository repository;
    private final GetPublicLegalEsnnaUseCase getPublicLegalEsnnaUseCase;

    public UpdateLegalEsnnaUseCase(LegalEsnnaRepository repository, GetPublicLegalEsnnaUseCase getPublicLegalEsnnaUseCase) {
        this.repository = repository;
        this.getPublicLegalEsnnaUseCase = getPublicLegalEsnnaUseCase;
    }

    @Audited(action = "UPDATE_LEGAL_ESNNA", entityType = "LEGAL_ESNNA")
    @Transactional
    public LegalEsnnaDTO execute(UpdateLegalEsnnaRequest req) {
        LegalEsnna page = repository.findSingleton()
            .orElseGet(() -> {
                LegalEsnna newP = new LegalEsnna();
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
        page.setDeclarationEyebrow(req.declarationEyebrow());
        page.setDeclarationTitle(req.declarationTitle());
        page.setDeclarationBody(req.declarationBody());
        page.setSections(toSections(req.sections()));
        page.setClosingTitle(req.closingTitle());
        page.setClosingBody(req.closingBody());
        page.setClosingLinkLabel(req.closingLinkLabel());

        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        LegalEsnna saved = repository.save(page);
        return getPublicLegalEsnnaUseCase.toDTO(saved);
    }

    private static List<LegalSection> toSections(List<LegalSectionDTO> sections) {
        if (sections == null) return List.of();
        return sections.stream().map(s -> new LegalSection(s.title(), s.body())).toList();
    }
}
