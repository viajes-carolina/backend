package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class GetPromotionBySlugUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public GetPromotionBySlugUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    public PromotionDTO execute(String slug) {
        Promotion promotion = promotionRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con slug: " + slug));

        List<Long> galleryIds = promotionRepository.findGalleryMediaIds(promotion.getId());
        return ListFeaturedPromotionsUseCase.mapToDTO(promotion, mediaRepository, galleryIds);
    }
}
