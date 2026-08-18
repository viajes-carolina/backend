package com.viajescarolina.api.about.infrastructure.persistence;

import com.viajescarolina.api.about.domain.AboutPage;
import com.viajescarolina.api.about.domain.AboutPageRepository;
import com.viajescarolina.api.media.infrastructure.persistence.MediaAssetPanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PanacheAboutPageRepository implements PanacheRepositoryBase<AboutPagePanacheEntity, Integer>, AboutPageRepository {

    @Override
    public Optional<AboutPage> findSingleton() {
        return findByIdOptional(1).map(this::enrichWithMediaUrls);
    }

    @Override
    public AboutPage save(AboutPage aboutPage) {
        AboutPagePanacheEntity entity = findById(1);
        if (entity == null) {
            entity = AboutPagePanacheEntity.fromDomain(aboutPage);
            persist(entity);
        } else {
            entity.heroBadge = aboutPage.getHeroBadge();
            entity.heroTitle = aboutPage.getHeroTitle();
            entity.heroSubtitle = aboutPage.getHeroSubtitle();
            entity.heroMediaId = aboutPage.getHeroMediaId();
            entity.storyTitle = aboutPage.getStoryTitle();
            entity.storyBody = aboutPage.getStoryBody();
            entity.storyMediaId = aboutPage.getStoryMediaId();
            entity.missionTitle = aboutPage.getMissionTitle();
            entity.missionBody = aboutPage.getMissionBody();
            entity.visionTitle = aboutPage.getVisionTitle();
            entity.visionBody = aboutPage.getVisionBody();
            entity.experienceYears = aboutPage.getExperienceYears();
            entity.happyTravelers = aboutPage.getHappyTravelers();
            entity.destinationsCount = aboutPage.getDestinationsCount();
            entity.satisfactionRatePercent = aboutPage.getSatisfactionRatePercent();
            entity.revision = aboutPage.getRevision();
            entity.updatedAt = aboutPage.getUpdatedAt();
            try {
                entity.valuesJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(aboutPage.getValues());
            } catch (Exception e) {
                entity.valuesJson = "[]";
            }
        }
        return enrichWithMediaUrls(entity);
    }

    private AboutPage enrichWithMediaUrls(AboutPagePanacheEntity entity) {
        String heroMediaUrl = null;
        if (entity.heroMediaId != null) {
            MediaAssetPanacheEntity media = MediaAssetPanacheEntity.findById(entity.heroMediaId);
            if (media != null) {
                heroMediaUrl = media.storagePath;
            }
        }

        String storyMediaUrl = null;
        if (entity.storyMediaId != null) {
            MediaAssetPanacheEntity media = MediaAssetPanacheEntity.findById(entity.storyMediaId);
            if (media != null) {
                storyMediaUrl = media.storagePath;
            }
        }

        return entity.toDomain(heroMediaUrl, storyMediaUrl);
    }
}
