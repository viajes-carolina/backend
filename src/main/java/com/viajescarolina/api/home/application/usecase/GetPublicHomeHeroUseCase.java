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
        String bgUrl = null;
        Double bgFocalX = 50.0;
        Double bgFocalY = 50.0;
        if (hero.getBackgroundMediaId() != null) {
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

        String cardMediaUrl = null;
        if (hero.getFeaturedCardMediaId() != null) {
            Optional<MediaAsset> cardAsset = mediaRepository.findMediaById(hero.getFeaturedCardMediaId());
            if (cardAsset.isPresent()) {
                cardMediaUrl = cardAsset.get().getStoragePath();
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
                hero.getFeaturedCardBadge(),
                hero.getFeaturedCardTitle(),
                hero.getFeaturedCardSubtitle(),
                hero.getFeaturedCardPricePen(),
                hero.getFeaturedCardOrigin(),
                hero.getFeaturedCardMediaId(),
                cardMediaUrl,
                hero.getRevision(),
                hero.getUpdatedAt()
        );
    }
}
