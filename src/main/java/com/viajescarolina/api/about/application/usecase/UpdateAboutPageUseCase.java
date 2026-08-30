package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.AboutPageDTO;
import com.viajescarolina.api.about.application.dto.AccompanyStepDTO;
import com.viajescarolina.api.about.application.dto.UpdateAboutPageRequest;
import com.viajescarolina.api.about.domain.AboutPage;
import com.viajescarolina.api.about.domain.AboutPageRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class UpdateAboutPageUseCase {
    private final AboutPageRepository aboutPageRepository;
    private final GetPublicAboutUseCase getPublicAboutUseCase;

    public UpdateAboutPageUseCase(AboutPageRepository aboutPageRepository, GetPublicAboutUseCase getPublicAboutUseCase) {
        this.aboutPageRepository = aboutPageRepository;
        this.getPublicAboutUseCase = getPublicAboutUseCase;
    }

    @Audited(action = "UPDATE_ABOUT_PAGE", entityType = "ABOUT_PAGE")
    @Transactional
    public AboutPageDTO execute(UpdateAboutPageRequest req) {
        AboutPage page = aboutPageRepository.findSingleton()
            .orElseGet(() -> {
                AboutPage newP = new AboutPage();
                newP.setId(1);
                newP.setRevision(1);
                newP.setCreatedAt(Instant.now());
                return newP;
            });

        page.setHeroBadge(req.heroBadge());
        page.setHeroTitle(req.heroTitle());
        page.setHeroSubtitle(req.heroSubtitle());
        page.setHeroCardBadge(req.heroCardBadge());
        page.setHeroCardTitle(req.heroCardTitle());
        page.setHeroCardLocation(req.heroCardLocation());
        page.setHeroCardDetail(req.heroCardDetail());
        page.setHeroNoteText(req.heroNoteText());

        page.setAccompanyBadge(req.accompanyBadge());
        page.setAccompanyTitle(req.accompanyTitle());
        page.setAccompanySubtitle(req.accompanySubtitle());
        page.setAccompanySteps(toAccompanySteps(req.accompanySteps()));
        page.setAccompanyQuote(req.accompanyQuote());

        page.setAdvisorsBadge(req.advisorsBadge());
        page.setAdvisorsHighlights(toAccompanySteps(req.advisorsHighlights()));

        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        AboutPage saved = aboutPageRepository.save(page);
        return getPublicAboutUseCase.toPageDTO(saved);
    }

    private static List<AboutPage.AccompanyStep> toAccompanySteps(List<AccompanyStepDTO> steps) {
        if (steps == null) return List.of();
        return steps.stream().map(s -> new AboutPage.AccompanyStep(s.title(), s.body())).toList();
    }
}
