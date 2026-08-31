package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.HomeFeaturedPolicy;
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

/**
 * Las promociones del bloque de Inicio, tal y como las sirve el endpoint público. Qué entra
 * en ese bloque lo decide {@link HomeFeaturedPolicy}, no este caso de uso.
 *
 * <p>Además aloja el mapeo compartido a {@link PromotionDTO} que reutilizan el resto de casos
 * de uso de promociones.</p>
 */
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
        // El criterio de qué entra en portada vive en HomeFeaturedPolicy y lo resuelve el
        // repositorio: aquí no se reimplementa ni el orden ni el límite.
        List<Promotion> promotions = promotionRepository.findHomeFeatured();

        // Batch-resolve las fotos destacadas UNA sola vez para todo el listado, en vez de
        // una query por promoción dentro del .map() (evita el N+1).
        Map<Long, MediaAsset> mediaById = resolveMediaMap(promotions, mediaRepository);

        // Sin marcar featuredInHome: aquí sería redundante (estas tres SON la portada) y el
        // contrato del endpoint público se mantiene byte a byte como estaba.
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
     *
     * <p>Deja {@code featuredInHome} sin calcular (null, y por tanto omitido del JSON). Es la
     * variante para respuestas de UNA promoción —alta, edición, mostrar/ocultar—, donde saber
     * si entra en portada exigiría una consulta extra en cada mutación y nadie la usa. Para
     * listados, {@link #mapToDTO(Promotion, Map, Set)}.</p>
     */
    public static PromotionDTO mapToDTO(Promotion promotion, Map<Long, MediaAsset> mediaById) {
        return toDTO(promotion, mediaById, null);
    }

    /**
     * Igual que {@link #mapToDTO(Promotion, Map)}, pero marcando si la promoción está en
     * portada según los IDs resueltos una sola vez para todo el listado con
     * {@link PromotionRepository#findHomeFeaturedIds()}.
     */
    public static PromotionDTO mapToDTO(Promotion promotion, Map<Long, MediaAsset> mediaById, Set<Long> homeFeaturedIds) {
        return toDTO(promotion, mediaById, homeFeaturedIds.contains(promotion.getId()));
    }

    private static PromotionDTO toDTO(Promotion promotion, Map<Long, MediaAsset> mediaById, Boolean featuredInHome) {
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
                promotion.getFacebookPermalinkUrl(),
                featuredInHome
        );
    }
}
