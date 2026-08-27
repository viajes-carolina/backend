package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ListAdminPromotionsUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListAdminPromotionsUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<PromotionDTO> execute() {
        List<Promotion> promotions = promotionRepository.findAllPromotions();

        // Batch-resolve las fotos destacadas UNA sola vez para TODAS las promociones
        // (hoy 32 filas y creciendo), en vez de una query por promoción dentro del .map().
        Map<Long, MediaAsset> mediaById = ListFeaturedPromotionsUseCase.resolveMediaMap(promotions, mediaRepository);

        return promotions.stream()
                .map(p -> ListFeaturedPromotionsUseCase.mapToDTO(p, mediaById))
                .toList();
    }
}
