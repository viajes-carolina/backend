package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.CookieCategoryDTO;
import com.viajescarolina.api.legal.application.dto.LegalCookiesDTO;
import com.viajescarolina.api.legal.application.dto.LegalSectionDTO;
import com.viajescarolina.api.legal.application.dto.UpdateLegalCookiesRequest;
import com.viajescarolina.api.legal.domain.CookieCategory;
import com.viajescarolina.api.legal.domain.LegalCookies;
import com.viajescarolina.api.legal.domain.LegalCookiesRepository;
import com.viajescarolina.api.legal.domain.LegalSection;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateLegalCookiesUseCase {

    private final LegalCookiesRepository repository;
    private final GetPublicLegalCookiesUseCase getPublicLegalCookiesUseCase;

    public UpdateLegalCookiesUseCase(LegalCookiesRepository repository, GetPublicLegalCookiesUseCase getPublicLegalCookiesUseCase) {
        this.repository = repository;
        this.getPublicLegalCookiesUseCase = getPublicLegalCookiesUseCase;
    }

    @Audited(action = "UPDATE_LEGAL_COOKIES", entityType = "LEGAL_COOKIES")
    @Transactional
    public LegalCookiesDTO execute(UpdateLegalCookiesRequest req) {
        LegalCookies page = repository.findSingleton()
            .orElseGet(() -> {
                LegalCookies newP = new LegalCookies();
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
        page.setCookieCategories(toCategories(req.cookieCategories()));
        page.setAcceptAllLabel(req.acceptAllLabel());
        page.setSavePreferencesLabel(req.savePreferencesLabel());

        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        LegalCookies saved = repository.save(page);
        return getPublicLegalCookiesUseCase.toDTO(saved);
    }

    private static List<LegalSection> toSections(List<LegalSectionDTO> sections) {
        if (sections == null) return List.of();
        return sections.stream().map(s -> new LegalSection(s.title(), s.body())).toList();
    }

    private static List<CookieCategory> toCategories(List<CookieCategoryDTO> categories) {
        if (categories == null) return List.of();
        return categories.stream()
            .map(c -> new CookieCategory(c.key(), c.name(), c.description(), c.required()))
            .toList();
    }
}
