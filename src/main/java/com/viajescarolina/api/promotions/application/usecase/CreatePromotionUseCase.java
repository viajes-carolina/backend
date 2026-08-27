package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.common.audit.Audited;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.media.domain.MediaStorageService;
import com.viajescarolina.api.promotions.application.dto.CreateOrUpdatePromotionRequest;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import com.viajescarolina.api.promotions.infrastructure.facebook.FacebookGraphClient;
import com.viajescarolina.api.promotions.infrastructure.facebook.FacebookPublishResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Crea una promoción a partir del formulario estructurado del admin (título, precio, fechas,
 * foto, inclusiones) y, en modo best-effort, publica ese mismo contenido como post con foto
 * en la Página de Facebook (ver {@link FacebookGraphClient#publishPhoto}). La publicación en
 * Facebook nunca condiciona la creación: si falla, no hay foto asociada, o la publicación está
 * deshabilitada, la promoción queda creada igual — solo se loguea el intento fallido.
 */
@ApplicationScoped
public class CreatePromotionUseCase {

    private static final Logger LOG = Logger.getLogger(CreatePromotionUseCase.class);
    // El slug se guarda en una columna varchar(120) — se deja margen para el sufijo "-N".
    private static final int MAX_SLUG_BASE_LENGTH = 110;

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;
    private final MediaStorageService mediaStorageService;
    private final FacebookGraphClient facebookGraphClient;

    @Inject
    public CreatePromotionUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository,
            MediaStorageService mediaStorageService,
            FacebookGraphClient facebookGraphClient) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
        this.mediaStorageService = mediaStorageService;
        this.facebookGraphClient = facebookGraphClient;
    }

    @Audited(action = "CREATE_PROMOTION", entityType = "PROMOTION")
    @Transactional
    public PromotionDTO execute(CreateOrUpdatePromotionRequest request) {
        String slug = buildUniqueSlug(request.title());

        Promotion promotion = Promotion.create(
                slug,
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
                request.inclusions(),
                request.exclusions(),
                request.whatsappMessageTemplate());

        Promotion saved = promotionRepository.save(promotion);
        saved = tryPublishToFacebook(saved);

        // Promoción única (respuesta de creación, no un listado): se resuelve con el mismo
        // contrato batch usando una lista de un solo elemento.
        List<Promotion> savedAsList = List.of(saved);
        Map<Long, MediaAsset> mediaById = ListFeaturedPromotionsUseCase.resolveMediaMap(savedAsList, mediaRepository);
        return ListFeaturedPromotionsUseCase.mapToDTO(saved, mediaById);
    }

    /**
     * Intenta publicar la promoción recién creada como post con foto en la Página de
     * Facebook. Cualquier excepción (lectura de la foto, llamada a la Graph API) se captura
     * y solo se loguea — jamás debe hacer fallar la creación de la promoción, que ya quedó
     * guardada exitosamente antes de llegar aquí.
     */
    private Promotion tryPublishToFacebook(Promotion promotion) {
        if (promotion.getFeaturedMediaId() == null) {
            return promotion;
        }
        try {
            Optional<MediaAsset> mediaAsset = mediaRepository.findMediaById(promotion.getFeaturedMediaId());
            if (mediaAsset.isEmpty()) {
                LOG.warn("Promoción " + promotion.getId() + " creada sin foto válida asociada (media_asset "
                        + promotion.getFeaturedMediaId() + " no encontrado); no se publica en Facebook.");
                return promotion;
            }

            byte[] photoBytes = readMediaBytes(mediaAsset.get().getFilename());
            if (photoBytes == null || photoBytes.length == 0) {
                LOG.warn("No se pudieron leer los bytes de la foto de la promoción " + promotion.getId()
                        + "; no se publica en Facebook.");
                return promotion;
            }

            String caption = buildCaption(promotion);
            Optional<FacebookPublishResult> published = facebookGraphClient.publishPhoto(
                    photoBytes, mediaAsset.get().getMimeType(), caption);

            if (published.isPresent()) {
                promotion.setFacebookPublishResult(published.get().postId(), published.get().permalinkUrl());
                return promotionRepository.save(promotion);
            }
            return promotion;
        } catch (Exception e) {
            LOG.error("Error intentando publicar la promoción " + promotion.getId() + " en Facebook; "
                    + "la promoción queda creada de todas formas.", e);
            return promotion;
        }
    }

    private byte[] readMediaBytes(String filename) throws java.io.IOException {
        try (InputStream in = mediaStorageService.retrieve(filename)) {
            return in.readAllBytes();
        }
    }

    private String buildCaption(Promotion promotion) {
        StringBuilder caption = new StringBuilder();
        caption.append(promotion.getTitle()).append("\n\n");
        caption.append(promotion.getSummary()).append("\n\n");

        List<String> inclusions = promotion.getInclusions();
        if (inclusions != null && !inclusions.isEmpty()) {
            caption.append(String.join("\n", inclusions)).append("\n\n");
        }

        caption.append("📅 ").append(promotion.getDurationDays()).append("D / ")
                .append(promotion.getDurationNights()).append("N\n");
        caption.append("💰 Desde USD ").append(promotion.getPriceUsd()).append("\n");
        caption.append("📍 Saliendo de ").append(promotion.getDepartureCity()).append("\n\n");
        caption.append("¿Te interesa? Escríbenos por WhatsApp y te ayudamos a reservar.");

        return caption.toString();
    }

    private String buildUniqueSlug(String title) {
        String base = slugify(title);
        if (base.isBlank()) {
            base = "promocion";
        }
        if (base.length() > MAX_SLUG_BASE_LENGTH) {
            base = base.substring(0, MAX_SLUG_BASE_LENGTH).replaceAll("-+$", "");
        }

        String slug = base;
        int attempt = 2;
        while (promotionRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + attempt;
            attempt++;
        }
        return slug;
    }

    private String slugify(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
