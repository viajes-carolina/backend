package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalMinceturDTO;
import com.viajescarolina.api.legal.application.dto.LegalSectionDTO;
import com.viajescarolina.api.legal.application.dto.UpdateLegalMinceturRequest;
import com.viajescarolina.api.legal.domain.LegalMincetur;
import com.viajescarolina.api.legal.domain.LegalMinceturRepository;
import com.viajescarolina.api.legal.domain.LegalSection;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateLegalMinceturUseCase {

    private final LegalMinceturRepository repository;
    private final GetPublicLegalMinceturUseCase getPublicLegalMinceturUseCase;

    public UpdateLegalMinceturUseCase(LegalMinceturRepository repository, GetPublicLegalMinceturUseCase getPublicLegalMinceturUseCase) {
        this.repository = repository;
        this.getPublicLegalMinceturUseCase = getPublicLegalMinceturUseCase;
    }

    @Audited(action = "UPDATE_LEGAL_MINCETUR", entityType = "LEGAL_MINCETUR")
    @Transactional
    public LegalMinceturDTO execute(UpdateLegalMinceturRequest req) {
        LegalMincetur page = repository.findSingleton()
            .orElseGet(() -> {
                LegalMincetur newP = new LegalMincetur();
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
        page.setVerificationEyebrow(req.verificationEyebrow());
        page.setVerificationButtonLabel(req.verificationButtonLabel());
        page.setVerificationNote(req.verificationNote());
        page.setClosingTitle(req.closingTitle());
        page.setClosingBody(req.closingBody());
        page.setClosingLinkLabel(req.closingLinkLabel());

        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        LegalMincetur saved = repository.save(page);
        return getPublicLegalMinceturUseCase.toDTO(saved);
    }

    private static List<LegalSection> toSections(List<LegalSectionDTO> sections) {
        if (sections == null) return List.of();
        return sections.stream().map(s -> new LegalSection(s.title(), s.body())).toList();
    }
}
