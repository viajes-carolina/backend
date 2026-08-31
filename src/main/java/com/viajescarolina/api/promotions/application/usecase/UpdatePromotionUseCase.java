package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.common.audit.Audited;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.CreateOrUpdatePromotionRequest;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * Corrige el contenido de una promoción ya existente con el mismo formulario estructurado que
 * se usa al crearla. Es el complemento de {@link CreatePromotionUseCase}: hasta ahora una
 * promoción mal cargada solo se podía arreglar borrándola y rehaciéndola, cosa que el guard de
 * "mínimo 3 activas" de {@code DeletePromotionUseCase} puede bloquear.
 *
 * <p>Diferencias explícitas con la creación:
 * <ul>
 *   <li><b>No republica en Facebook.</b> {@link CreatePromotionUseCase} publica la promoción
 *       como post con foto en la Página (best-effort). Editar NO vuelve a llamar a
 *       {@code FacebookGraphClient}: la Graph API crearía un post nuevo, no editaría el
 *       existente, y el resultado sería contenido duplicado en la Página. Por eso este caso de
 *       uso ni siquiera inyecta el cliente de Facebook, y {@code facebookPostId} /
 *       {@code facebookPermalinkUrl} se conservan apuntando al post original.</li>
 *   <li><b>No regenera el slug</b> aunque cambie el título (ver
 *       {@link Promotion#updateEditableDetails}).</li>
 *   <li><b>No toca {@code active} ni {@code source}</b>: no viajan en el request y la
 *       visibilidad sigue siendo responsabilidad de {@code SetPromotionActiveUseCase}.</li>
 * </ul>
 */
@ApplicationScoped
public class UpdatePromotionUseCase {

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public UpdatePromotionUseCase(PromotionRepository promotionRepository, MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "UPDATE_PROMOTION", entityType = "PROMOTION")
    @Transactional
    public PromotionDTO execute(Long id, CreateOrUpdatePromotionRequest request) {
        Promotion promotion = promotionRepository.findPromotionById(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));

        // La foto destacada es FK a media_asset: se valida antes de guardar para responder 404
        // con un mensaje claro en vez de reventar con una violación de integridad.
        Long featuredMediaId = request.featuredMediaId();
        if (featuredMediaId != null && mediaRepository.findMediaById(featuredMediaId).isEmpty()) {
            throw new NotFoundException("Foto no encontrada con ID: " + featuredMediaId);
        }

        promotion.updateEditableDetails(
                trimmed(request.title()),
                trimmed(request.destination()),
                trimmed(request.summary()),
                request.priceUsd(),
                request.pricePen(),
                request.durationDays(),
                request.durationNights(),
                trimmed(request.departureCity()),
                request.validFrom(),
                request.validUntil(),
                featuredMediaId,
                request.inclusions(),
                request.exclusions(),
                trimmed(request.whatsappMessageTemplate()));

        Promotion saved = promotionRepository.save(promotion);

        // Promoción única (respuesta de actualización, no un listado): mismo contrato batch
        // con una lista de un solo elemento.
        List<Promotion> savedAsList = List.of(saved);
        Map<Long, MediaAsset> mediaById = ListFeaturedPromotionsUseCase.resolveMediaMap(savedAsList, mediaRepository);
        return ListFeaturedPromotionsUseCase.mapToDTO(saved, mediaById);
    }

    private String trimmed(String value) {
        return value != null ? value.trim() : null;
    }
}
