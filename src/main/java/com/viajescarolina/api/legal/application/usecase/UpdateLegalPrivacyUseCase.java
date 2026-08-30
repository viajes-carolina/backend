package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.LegalPrivacyDTO;
import com.viajescarolina.api.legal.application.dto.LegalSectionDTO;
import com.viajescarolina.api.legal.application.dto.UpdateLegalPrivacyRequest;
import com.viajescarolina.api.legal.domain.LegalPrivacy;
import com.viajescarolina.api.legal.domain.LegalPrivacyRepository;
import com.viajescarolina.api.legal.domain.LegalSection;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateLegalPrivacyUseCase {

    private final LegalPrivacyRepository repository;
    private final GetPublicLegalPrivacyUseCase getPublicLegalPrivacyUseCase;

    public UpdateLegalPrivacyUseCase(LegalPrivacyRepository repository, GetPublicLegalPrivacyUseCase getPublicLegalPrivacyUseCase) {
        this.repository = repository;
        this.getPublicLegalPrivacyUseCase = getPublicLegalPrivacyUseCase;
    }

    @Audited(action = "UPDATE_LEGAL_PRIVACY", entityType = "LEGAL_PRIVACY")
    @Transactional
    public LegalPrivacyDTO execute(UpdateLegalPrivacyRequest req) {
        LegalPrivacy page = repository.findSingleton()
            .orElseGet(() -> {
                LegalPrivacy newP = new LegalPrivacy();
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

        LegalPrivacy saved = repository.save(page);
        return getPublicLegalPrivacyUseCase.toDTO(saved);
    }

    private static List<LegalSection> toSections(List<LegalSectionDTO> sections) {
        if (sections == null) return List.of();
        return sections.stream().map(s -> new LegalSection(s.title(), s.body())).toList();
    }
}
