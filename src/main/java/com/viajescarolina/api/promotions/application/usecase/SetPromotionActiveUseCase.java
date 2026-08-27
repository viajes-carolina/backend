package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.common.audit.Audited;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SetPromotionActiveUseCase {

    private static final int MIN_ACTIVE_POOL = 3;

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public SetPromotionActiveUseCase(PromotionRepository promotionRepository, MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "SET_PROMOTION_ACTIVE", entityType = "PROMOTION")
    @Transactional
    public PromotionDTO execute(Long id, boolean active) {
        Promotion promotion = promotionRepository.findPromotionById(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));

        if (!active && promotion.isActive() && promotionRepository.countActive() <= MIN_ACTIVE_POOL) {
            throw new WebApplicationException(
                    "No se puede ocultar esta promoción: quedarían menos de 3 promociones activas para mostrar en Inicio.",
                    Response.Status.CONFLICT);
        }

        promotion.setActive(active);
        Promotion saved = promotionRepository.save(promotion);

        // Promoción única (no un listado): mismo contrato batch con una lista de un elemento.
        List<Promotion> savedAsList = List.of(saved);
        Map<Long, MediaAsset> mediaById = ListFeaturedPromotionsUseCase.resolveMediaMap(savedAsList, mediaRepository);
        return ListFeaturedPromotionsUseCase.mapToDTO(saved, mediaById);
    }
}
