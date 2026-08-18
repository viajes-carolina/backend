package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ListFeaturedPromotionsUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListFeaturedPromotionsUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<PromotionDTO> execute() {
        return promotionRepository.findFeatured().stream()
                .map(p -> mapToDTO(p, mediaRepository))
                .toList();
    }

    public static PromotionDTO mapToDTO(Promotion promotion, MediaRepository mediaRepository) {
        String mediaUrl = null;
        Double focalX = 50.0;
        Double focalY = 50.0;

        if (promotion.getFeaturedMediaId() != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(promotion.getFeaturedMediaId());
            if (asset.isPresent()) {
                mediaUrl = asset.get().getStoragePath();
                if (asset.get().getFocalX() != null) {
                    focalX = asset.get().getFocalX().doubleValue();
                }
                if (asset.get().getFocalY() != null) {
                    focalY = asset.get().getFocalY().doubleValue();
                }
            }
        }

        return new PromotionDTO(
                promotion.getId(),
                promotion.getSlug(),
                promotion.getTitle(),
                promotion.getDestination(),
                promotion.getSummary(),
                promotion.getPriceUsd(),
                promotion.getPricePen(),
                promotion.getDurationDays(),
                promotion.getDurationNights(),
                promotion.getDepartureCity(),
                promotion.getValidFrom(),
                promotion.getValidUntil(),
                promotion.getFeaturedMediaId(),
                mediaUrl,
                focalX,
                focalY,
                promotion.isFeatured(),
                promotion.getInclusions(),
                promotion.getExclusions(),
                promotion.getWhatsappMessageTemplate(),
                promotion.getDisplayOrder(),
                promotion.isActive(),
                promotion.getCreatedAt(),
                promotion.getUpdatedAt()
        );
    }
}
