package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeHeroDTO;
import com.viajescarolina.api.home.application.dto.UpdateHomeHeroRequest;
import com.viajescarolina.api.home.domain.HomeHero;
import com.viajescarolina.api.home.domain.HomeHeroRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateHomeHeroUseCase {

    private final HomeHeroRepository homeHeroRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public UpdateHomeHeroUseCase(HomeHeroRepository homeHeroRepository, MediaRepository mediaRepository) {
        this.homeHeroRepository = homeHeroRepository;
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public HomeHeroDTO execute(UpdateHomeHeroRequest request) {
        HomeHero hero = homeHeroRepository.findHero()
                .orElseThrow(() -> new NotFoundException("Configuración de Home Hero no encontrada"));

        Long validBgMediaId = null;
        if (request.backgroundMediaId() != null && request.backgroundMediaId() > 0) {
            if (mediaRepository.findMediaById(request.backgroundMediaId()).isPresent()) {
                validBgMediaId = request.backgroundMediaId();
            }
        }

        Long validCardMediaId = null;
        if (request.featuredCardMediaId() != null && request.featuredCardMediaId() > 0) {
            if (mediaRepository.findMediaById(request.featuredCardMediaId()).isPresent()) {
                validCardMediaId = request.featuredCardMediaId();
            }
        }

        hero.update(
                request.badgeText(),
                request.titleHighlight(),
                request.titleAccent(),
                request.description(),
                request.whatsappCtaText(),
                request.whatsappMessageOverride(),
                request.secondaryCtaText(),
                request.secondaryCtaUrl(),
                request.trustIndicators(),
                validBgMediaId,
                request.backgroundMediaUrl(),
                request.backgroundFocalX(),
                request.backgroundFocalY(),
                request.featuredCardBadge(),
                request.featuredCardTitle(),
                request.featuredCardSubtitle(),
                request.featuredCardPricePen(),
                request.featuredCardOrigin(),
                validCardMediaId,
                request.featuredCardMediaUrl()
        );

        HomeHero saved = homeHeroRepository.save(hero);
        return GetPublicHomeHeroUseCase.mapToDTO(saved, mediaRepository);
    }
}
