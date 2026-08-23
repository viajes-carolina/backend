package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeHeroDTO;
import com.viajescarolina.api.home.domain.HomeHero;
import com.viajescarolina.api.home.domain.HomeHeroRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.Optional;

@ApplicationScoped
public class GetPublicHomeHeroUseCase {

    private final HomeHeroRepository homeHeroRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public GetPublicHomeHeroUseCase(HomeHeroRepository homeHeroRepository, MediaRepository mediaRepository) {
        this.homeHeroRepository = homeHeroRepository;
        this.mediaRepository = mediaRepository;
    }

    public HomeHeroDTO execute() {
        HomeHero hero = homeHeroRepository.findHero()
                .orElseThrow(() -> new NotFoundException("Configuración de Home Hero no encontrada"));

        return mapToDTO(hero, mediaRepository);
    }

    public static HomeHeroDTO mapToDTO(HomeHero hero, MediaRepository mediaRepository) {
        String bgUrl = hero.getBackgroundMediaUrl();
        Double bgFocalX = hero.getBackgroundFocalX() != null ? hero.getBackgroundFocalX() : 50.0;
        Double bgFocalY = hero.getBackgroundFocalY() != null ? hero.getBackgroundFocalY() : 50.0;

        if ((bgUrl == null || bgUrl.isBlank()) && hero.getBackgroundMediaId() != null) {
            Optional<MediaAsset> bgAsset = mediaRepository.findMediaById(hero.getBackgroundMediaId());
            if (bgAsset.isPresent()) {
                bgUrl = bgAsset.get().getStoragePath();
                if (bgAsset.get().getFocalX() != null) {
                    bgFocalX = bgAsset.get().getFocalX().doubleValue();
                }
                if (bgAsset.get().getFocalY() != null) {
                    bgFocalY = bgAsset.get().getFocalY().doubleValue();
                }
            }
        }

        if (bgUrl == null || bgUrl.isBlank()) {
            bgUrl = "/media/demo-cartagena-caribe.webp";
        }

        String secondary1Url = hero.getSecondaryMedia1Url();
        Double secondary1FocalX = hero.getSecondaryMedia1FocalX() != null ? hero.getSecondaryMedia1FocalX() : 50.0;
        Double secondary1FocalY = hero.getSecondaryMedia1FocalY() != null ? hero.getSecondaryMedia1FocalY() : 50.0;
        if ((secondary1Url == null || secondary1Url.isBlank()) && hero.getSecondaryMedia1Id() != null) {
            Optional<MediaAsset> secondary1Asset = mediaRepository.findMediaById(hero.getSecondaryMedia1Id());
            if (secondary1Asset.isPresent()) {
                secondary1Url = secondary1Asset.get().getStoragePath();
                if (secondary1Asset.get().getFocalX() != null) {
                    secondary1FocalX = secondary1Asset.get().getFocalX().doubleValue();
                }
                if (secondary1Asset.get().getFocalY() != null) {
                    secondary1FocalY = secondary1Asset.get().getFocalY().doubleValue();
                }
            }
        }
        // Sin fallback a foto demo: vacío se queda vacío, el frontend muestra
        // un placeholder abstracto (degradado + ícono) hasta que el admin suba la foto.

        String secondary2Url = hero.getSecondaryMedia2Url();
        Double secondary2FocalX = hero.getSecondaryMedia2FocalX() != null ? hero.getSecondaryMedia2FocalX() : 50.0;
        Double secondary2FocalY = hero.getSecondaryMedia2FocalY() != null ? hero.getSecondaryMedia2FocalY() : 50.0;
        if ((secondary2Url == null || secondary2Url.isBlank()) && hero.getSecondaryMedia2Id() != null) {
            Optional<MediaAsset> secondary2Asset = mediaRepository.findMediaById(hero.getSecondaryMedia2Id());
            if (secondary2Asset.isPresent()) {
                secondary2Url = secondary2Asset.get().getStoragePath();
                if (secondary2Asset.get().getFocalX() != null) {
                    secondary2FocalX = secondary2Asset.get().getFocalX().doubleValue();
                }
                if (secondary2Asset.get().getFocalY() != null) {
                    secondary2FocalY = secondary2Asset.get().getFocalY().doubleValue();
                }
            }
        }

        String secondary3Url = hero.getSecondaryMedia3Url();
        Double secondary3FocalX = hero.getSecondaryMedia3FocalX() != null ? hero.getSecondaryMedia3FocalX() : 50.0;
        Double secondary3FocalY = hero.getSecondaryMedia3FocalY() != null ? hero.getSecondaryMedia3FocalY() : 50.0;
        if ((secondary3Url == null || secondary3Url.isBlank()) && hero.getSecondaryMedia3Id() != null) {
            Optional<MediaAsset> secondary3Asset = mediaRepository.findMediaById(hero.getSecondaryMedia3Id());
            if (secondary3Asset.isPresent()) {
                secondary3Url = secondary3Asset.get().getStoragePath();
                if (secondary3Asset.get().getFocalX() != null) {
                    secondary3FocalX = secondary3Asset.get().getFocalX().doubleValue();
                }
                if (secondary3Asset.get().getFocalY() != null) {
                    secondary3FocalY = secondary3Asset.get().getFocalY().doubleValue();
                }
            }
        }

        return new HomeHeroDTO(
                hero.getId(),
                hero.getBadgeText(),
                hero.getTitleHighlight(),
                hero.getTitleAccent(),
                hero.getDescription(),
                hero.getWhatsappCtaText(),
                hero.getWhatsappMessageOverride(),
                hero.getSecondaryCtaText(),
                hero.getSecondaryCtaUrl(),
                hero.getTrustIndicators(),
                hero.getBackgroundMediaId(),
                bgUrl,
                bgFocalX,
                bgFocalY,
                hero.getSecondaryMedia1Id(),
                secondary1Url,
                secondary1FocalX,
                secondary1FocalY,
                hero.getSecondaryMedia2Id(),
                secondary2Url,
                secondary2FocalX,
                secondary2FocalY,
                hero.getSecondaryMedia3Id(),
                secondary3Url,
                secondary3FocalX,
                secondary3FocalY,
                hero.getTrustStatText(),
                hero.getEyebrowText(),
                hero.getRevision(),
                hero.getUpdatedAt()
        );
    }
}
