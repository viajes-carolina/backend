package com.viajescarolina.api.about.application.usecase;

import com.viajescarolina.api.about.application.dto.AboutPageDTO;
import com.viajescarolina.api.about.application.dto.UpdateAboutPageRequest;
import com.viajescarolina.api.about.domain.AboutPage;
import com.viajescarolina.api.about.domain.AboutPageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class UpdateAboutPageUseCase {
    private final AboutPageRepository aboutPageRepository;
    private final GetPublicAboutUseCase getPublicAboutUseCase;

    public UpdateAboutPageUseCase(AboutPageRepository aboutPageRepository, GetPublicAboutUseCase getPublicAboutUseCase) {
        this.aboutPageRepository = aboutPageRepository;
        this.getPublicAboutUseCase = getPublicAboutUseCase;
    }

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
        page.setHeroMediaId(req.heroMediaId());
        page.setStoryTitle(req.storyTitle());
        page.setStoryBody(req.storyBody());
        page.setStoryMediaId(req.storyMediaId());
        page.setMissionTitle(req.missionTitle());
        page.setMissionBody(req.missionBody());
        page.setVisionTitle(req.visionTitle());
        page.setVisionBody(req.visionBody());
        page.setValues(req.values());
        page.setExperienceYears(req.experienceYears());
        page.setHappyTravelers(req.happyTravelers());
        page.setDestinationsCount(req.destinationsCount());
        page.setSatisfactionRatePercent(req.satisfactionRatePercent());
        page.setRevision(page.getRevision() + 1);
        page.setUpdatedAt(Instant.now());

        AboutPage saved = aboutPageRepository.save(page);
        return getPublicAboutUseCase.toPageDTO(saved);
    }
}
