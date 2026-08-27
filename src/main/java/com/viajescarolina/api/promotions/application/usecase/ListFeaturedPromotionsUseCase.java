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
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListFeaturedPromotionsUseCase {

    private static final int HOME_PROMOTIONS_LIMIT = 3;

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
        List<Promotion> promotions = promotionRepository.findTopActiveByRecency(HOME_PROMOTIONS_LIMIT);

        // Batch-resolve las fotos destacadas UNA sola vez para todo el listado, en vez de
        // una query por promoción dentro del .map() (evita el N+1).
        Map<Long, MediaAsset> mediaById = resolveMediaMap(promotions, mediaRepository);

        return promotions.stream()
                .map(p -> mapToDTO(p, mediaById))
                .toList();
    }

    /**
     * Recolecta los {@code featuredMediaId} (no nulos) de una lista de promociones y los
     * resuelve en una sola consulta batch. Pensado para llamarse una vez antes de mapear
     * una lista completa de promociones a DTO, nunca dentro de un loop por promoción.
     */
    public static Map<Long, MediaAsset> resolveMediaMap(List<Promotion> promotions, MediaRepository mediaRepository) {
        Set<Long> mediaIds = promotions.stream()
                .map(Promotion::getFeaturedMediaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (mediaIds.isEmpty()) {
            return Map.of();
        }
        return mediaRepository.findMediaByIds(mediaIds).stream()
                .collect(Collectors.toMap(MediaAsset::getId, Function.identity()));
    }

    /**
     * Mapea una {@link Promotion} de dominio a su DTO usando el mapa de medios YA resuelto
     * en batch (ver {@link #resolveMediaMap}). Este método permanece estático y puro: solo
     * hace lookups en memoria, sin acceso a BD.
     */
    public static PromotionDTO mapToDTO(Promotion promotion, Map<Long, MediaAsset> mediaById) {
        String mediaUrl = null;
        Double focalX = 50.0;
        Double focalY = 50.0;

        if (promotion.getFeaturedMediaId() != null) {
            MediaAsset asset = mediaById.get(promotion.getFeaturedMediaId());
            if (asset != null) {
                mediaUrl = asset.getStoragePath();
                if (asset.getFocalX() != null) {
                    focalX = asset.getFocalX().doubleValue();
                }
                if (asset.getFocalY() != null) {
                    focalY = asset.getFocalY().doubleValue();
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
                promotion.getInclusions(),
                promotion.getExclusions(),
                promotion.getWhatsappMessageTemplate(),
                promotion.isActive(),
                promotion.getCreatedAt(),
                promotion.getUpdatedAt(),
                promotion.getSource(),
                promotion.getFacebookPostId(),
                promotion.getFacebookPermalinkUrl()
        );
    }
}
