package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.CreateOrUpdatePromotionRequest;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdatePromotionUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public UpdatePromotionUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "UPDATE_PROMOTION", entityType = "PROMOTION")
    @Transactional
    public PromotionDTO execute(Long id, CreateOrUpdatePromotionRequest request) {
        Promotion promotion = promotionRepository.findPromotionById(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));

        promotion.update(
                request.slug(),
                request.title(),
                request.destination(),
                request.summary(),
                request.priceUsd(),
                request.pricePen(),
                request.durationDays(),
                request.durationNights(),
                request.departureCity() != null ? request.departureCity() : promotion.getDepartureCity(),
                request.validFrom() != null ? request.validFrom() : promotion.getValidFrom(),
                request.validUntil() != null ? request.validUntil() : promotion.getValidUntil(),
                request.featuredMediaId(),
                request.isFeatured() != null ? request.isFeatured() : promotion.isFeatured(),
                request.inclusions(),
                request.exclusions(),
                request.whatsappMessageTemplate(),
                request.displayOrder() != null ? request.displayOrder() : promotion.getDisplayOrder(),
                request.active() != null ? request.active() : promotion.isActive()
        );

        Promotion saved = promotionRepository.save(promotion);
        return ListFeaturedPromotionsUseCase.mapToDTO(saved, mediaRepository);
    }
}
