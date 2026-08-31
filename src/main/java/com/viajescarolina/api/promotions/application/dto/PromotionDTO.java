package com.viajescarolina.api.promotions.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Una promoción tal y como la ven el sitio público y el panel.
 *
 * @param featuredInHome {@code true} si esta promoción es una de las que Inicio muestra ahora
 *                       mismo. Es un dato DERIVADO (ver {@code HomeFeaturedPolicy}), no una
 *                       columna, y lo calcula el servidor porque es el único que ve el catálogo
 *                       entero: con el listado paginado, el panel solo tiene 15 filas delante y
 *                       no puede deducir cuáles entran en la portada.
 *                       <p>Viene {@code null} —y por tanto se omite del JSON, con
 *                       {@code serialization-inclusion=non-null}— en las respuestas de una sola
 *                       promoción (alta, edición, mostrar/ocultar): ahí no se calcula, y es
 *                       preferible callar a afirmar un {@code false} que puede ser mentira.</p>
 */
public record PromotionDTO(
        Long id,
        String slug,
        String title,
        String destination,
        String summary,
        BigDecimal priceUsd,
        BigDecimal pricePen,
        Integer durationDays,
        Integer durationNights,
        String departureCity,
        LocalDate validFrom,
        LocalDate validUntil,
        Long featuredMediaId,
        String featuredMediaUrl,
        Double featuredMediaFocalX,
        Double featuredMediaFocalY,
        List<String> inclusions,
        List<String> exclusions,
        String whatsappMessageTemplate,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        String source,
        String facebookPostId,
        String facebookPermalinkUrl,
        Boolean featuredInHome
) {}
