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
            entity.copyFrom(aboutPage);
        }
        return enrichWithMediaUrls(entity);
    }

    private AboutPage enrichWithMediaUrls(AboutPagePanacheEntity entity) {
        String heroMediaUrl = resolveMediaUrl(entity.heroMediaId);
        String storyMediaUrl = resolveMediaUrl(entity.storyMediaId);
        String momentsMediaUrl = resolveMediaUrl(entity.momentsMediaId);
        return entity.toDomain(heroMediaUrl, storyMediaUrl, momentsMediaUrl);
    }

    private String resolveMediaUrl(Long mediaId) {
        if (mediaId == null) {
            return null;
        }
        MediaAssetPanacheEntity media = MediaAssetPanacheEntity.findById(mediaId);
        return media != null ? media.storagePath : null;
    }
}
