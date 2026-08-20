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
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class CreatePromotionUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public CreatePromotionUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "CREATE_PROMOTION", entityType = "PROMOTION")
    @Transactional
    public PromotionDTO execute(CreateOrUpdatePromotionRequest request) {
        if (promotionRepository.findBySlug(request.slug()).isPresent()) {
            throw new BadRequestException("Ya existe una promoción con el slug: " + request.slug());
        }

        Promotion promotion = Promotion.create(
                request.slug(),
                request.title(),
                request.destination(),
                request.summary(),
                request.priceUsd(),
                request.pricePen(),
                request.durationDays(),
                request.durationNights(),
                request.departureCity(),
                request.validFrom(),
                request.validUntil(),
                request.featuredMediaId(),
                request.isFeatured() != null ? request.isFeatured() : true,
                request.inclusions(),
                request.exclusions(),
                request.whatsappMessageTemplate(),
                request.displayOrder()
        );

        Promotion saved = promotionRepository.save(promotion);
        return ListFeaturedPromotionsUseCase.mapToDTO(saved, mediaRepository);
    }
}
