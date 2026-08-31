package com.viajescarolina.api.promotions.domain;

/**
 * Contadores del catálogo COMPLETO de promociones, sin aplicar ningún filtro del listado.
 *
 * <p>Alimentan las métricas de la cabecera del panel, que deben seguir describiendo todo el
 * catálogo aunque el usuario esté filtrando o mirando la página 3. Por eso son deliberadamente
 * independientes del {@code total} de la página, que sí cuenta solo lo que cumple los filtros.</p>
 *
 * <p>No incluye "en portada": ese no es un conteo de filas sino un derivado de
 * {@link HomeFeaturedPolicy}, y se obtiene de {@link PromotionRepository#findHomeFeaturedIds()}
 * para no reimplementar el criterio en una segunda consulta.</p>
 *
 * @param total               todas las promociones existentes.
 * @param publishedOnFacebook las que tienen {@code facebook_permalink_url}, es decir, las que
 *                            tienen un post publicado en la Página.
 * @param hidden              las que están ocultas ({@code active = false}).
 */
public record PromotionCatalogCounters(long total, long publishedOnFacebook, long hidden) {
}
