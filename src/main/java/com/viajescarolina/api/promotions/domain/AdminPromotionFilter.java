package com.viajescarolina.api.promotions.domain;

import java.util.List;

/**
 * Criterios de búsqueda del listado administrativo de promociones. Todos son opcionales y
 * {@code null} significa siempre "sin filtrar por esto".
 *
 * @param search          texto libre que debe aparecer en el título, el destino o el resumen.
 *                        {@code null} o en blanco = sin búsqueda.
 * @param status          estado derivado (visible / oculta / vencida). {@code null} = todas.
 * @param source          origen de la promoción. {@code null} = todos.
 * @param featuredInHome  {@code TRUE} para quedarse solo con las que hoy ocupan la portada,
 *                        {@code FALSE} para excluirlas, {@code null} = todas.
 * @param homeFeaturedIds los IDs que hoy ocupan la portada, YA resueltos con el criterio único
 *                        de {@link HomeFeaturedPolicy} vía
 *                        {@link PromotionRepository#findHomeFeaturedIds()}. Viajan dentro del
 *                        filtro para que la consulta pueda resolver {@code featuredInHome} en
 *                        la base de datos sin reimplementar el criterio, y para que la marca
 *                        que se pinta en cada fila y el filtro que las selecciona salgan
 *                        exactamente del mismo cálculo. Solo se usan si
 *                        {@code featuredInHome != null}.
 */
public record AdminPromotionFilter(
        String search,
        PromotionStatusFilter status,
        PromotionSourceFilter source,
        Boolean featuredInHome,
        List<Long> homeFeaturedIds) {

    public AdminPromotionFilter {
        search = (search == null || search.isBlank()) ? null : search.trim();
        homeFeaturedIds = homeFeaturedIds == null ? List.of() : List.copyOf(homeFeaturedIds);
    }

    public boolean hasSearch() {
        return search != null;
    }
}
