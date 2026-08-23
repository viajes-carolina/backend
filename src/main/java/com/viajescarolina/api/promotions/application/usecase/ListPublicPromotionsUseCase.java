package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListPublicPromotionsUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListPublicPromotionsUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<PromotionDTO> execute() {
        return promotionRepository.findAllActive().stream()
                .map(p -> ListFeaturedPromotionsUseCase.mapToDTO(p, mediaRepository, promotionRepository.findGalleryMediaIds(p.getId())))
                .toList();
    }
}
