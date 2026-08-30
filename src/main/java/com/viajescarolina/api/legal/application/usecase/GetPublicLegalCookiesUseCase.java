package com.viajescarolina.api.legal.application.usecase;

import com.viajescarolina.api.legal.application.dto.CookieCategoryDTO;
import com.viajescarolina.api.legal.application.dto.LegalCookiesDTO;
import com.viajescarolina.api.legal.domain.CookieCategory;
import com.viajescarolina.api.legal.domain.LegalCookies;
import com.viajescarolina.api.legal.domain.LegalCookiesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GetPublicLegalCookiesUseCase {

    private final LegalCookiesRepository repository;

    public GetPublicLegalCookiesUseCase(LegalCookiesRepository repository) {
        this.repository = repository;
    }

    public LegalCookiesDTO execute() {
        LegalCookies page = repository.findSingleton()
            .orElseThrow(() -> new IllegalStateException("Datos de Política de cookies no inicializados"));
        return toDTO(page);
    }

    public LegalCookiesDTO toDTO(LegalCookies p) {
        return new LegalCookiesDTO(
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
            toCategoryDTOs(p.getCookieCategories()),
            p.getAcceptAllLabel(),
            p.getSavePreferencesLabel(),
            p.getRevision(),
            p.getUpdatedAt()
        );
    }

    static List<CookieCategoryDTO> toCategoryDTOs(List<CookieCategory> categories) {
        if (categories == null) return List.of();
        return categories.stream()
            .map(c -> new CookieCategoryDTO(c.key(), c.name(), c.description(), c.required()))
            .toList();
    }
}
