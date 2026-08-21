package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeHeroDTO;
import com.viajescarolina.api.home.application.dto.UpdateHomeHeroRequest;
import com.viajescarolina.api.home.domain.HomeHero;
import com.viajescarolina.api.home.domain.HomeHeroRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.common.audit.Audited;
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

    @Audited(action = "UPDATE_HOME_HERO", entityType = "HOME_HERO")
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

        Long validSecondary1MediaId = null;
        if (request.secondaryMedia1Id() != null && request.secondaryMedia1Id() > 0) {
            if (mediaRepository.findMediaById(request.secondaryMedia1Id()).isPresent()) {
                validSecondary1MediaId = request.secondaryMedia1Id();
            }
        }

        Long validSecondary2MediaId = null;
        if (request.secondaryMedia2Id() != null && request.secondaryMedia2Id() > 0) {
            if (mediaRepository.findMediaById(request.secondaryMedia2Id()).isPresent()) {
                validSecondary2MediaId = request.secondaryMedia2Id();
            }
        }

        Long validSecondary3MediaId = null;
        if (request.secondaryMedia3Id() != null && request.secondaryMedia3Id() > 0) {
            if (mediaRepository.findMediaById(request.secondaryMedia3Id()).isPresent()) {
                validSecondary3MediaId = request.secondaryMedia3Id();
            }
        }

        Long validSecondary4MediaId = null;
        if (request.secondaryMedia4Id() != null && request.secondaryMedia4Id() > 0) {
            if (mediaRepository.findMediaById(request.secondaryMedia4Id()).isPresent()) {
                validSecondary4MediaId = request.secondaryMedia4Id();
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
                request.featuredCardMediaUrl(),
                validSecondary1MediaId,
                request.secondaryMedia1Url(),
                request.secondaryMedia1FocalX(),
                request.secondaryMedia1FocalY(),
                validSecondary2MediaId,
                request.secondaryMedia2Url(),
                request.secondaryMedia2FocalX(),
                request.secondaryMedia2FocalY(),
                validSecondary3MediaId,
                request.secondaryMedia3Url(),
                request.secondaryMedia3FocalX(),
                request.secondaryMedia3FocalY(),
                validSecondary4MediaId,
                request.secondaryMedia4Url(),
                request.secondaryMedia4FocalX(),
                request.secondaryMedia4FocalY(),
                request.trustStatText(),
                request.eyebrowText()
        );

        HomeHero saved = homeHeroRepository.save(hero);
        return GetPublicHomeHeroUseCase.mapToDTO(saved, mediaRepository);
    }
}
