package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeHeroDTO;
import com.viajescarolina.api.home.domain.HomeHero;
import com.viajescarolina.api.home.domain.HomeHeroRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.domain.MediaResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

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
        MediaResolver.ResolvedMedia bg = MediaResolver.resolve(
                hero.getBackgroundMediaId(),
                hero.getBackgroundMediaUrl(),
                hero.getBackgroundFocalX(),
                hero.getBackgroundFocalY(),
                mediaRepository
        );
        String bgUrl = bg.url();
        double bgFocalX = bg.focalX();
        double bgFocalY = bg.focalY();

        if (bgUrl == null || bgUrl.isBlank()) {
            bgUrl = "/media/demo-cartagena-caribe.webp";
        }

        // Sin fallback a foto demo: vacío se queda vacío, el frontend muestra
        // un placeholder abstracto (degradado + ícono) hasta que el admin suba la foto.
        MediaResolver.ResolvedMedia secondary1 = MediaResolver.resolve(
                hero.getSecondaryMedia1Id(),
                hero.getSecondaryMedia1Url(),
                hero.getSecondaryMedia1FocalX(),
                hero.getSecondaryMedia1FocalY(),
                mediaRepository
        );
        String secondary1Url = secondary1.url();
        double secondary1FocalX = secondary1.focalX();
        double secondary1FocalY = secondary1.focalY();

        MediaResolver.ResolvedMedia secondary2 = MediaResolver.resolve(
                hero.getSecondaryMedia2Id(),
                hero.getSecondaryMedia2Url(),
                hero.getSecondaryMedia2FocalX(),
                hero.getSecondaryMedia2FocalY(),
                mediaRepository
        );
        String secondary2Url = secondary2.url();
        double secondary2FocalX = secondary2.focalX();
        double secondary2FocalY = secondary2.focalY();

        MediaResolver.ResolvedMedia secondary3 = MediaResolver.resolve(
                hero.getSecondaryMedia3Id(),
                hero.getSecondaryMedia3Url(),
                hero.getSecondaryMedia3FocalX(),
                hero.getSecondaryMedia3FocalY(),
                mediaRepository
        );
        String secondary3Url = secondary3.url();
        double secondary3FocalX = secondary3.focalX();
        double secondary3FocalY = secondary3.focalY();

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
