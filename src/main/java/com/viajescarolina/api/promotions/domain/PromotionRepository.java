package com.viajescarolina.api.promotions.domain;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    /**
     * Las promociones que AHORA MISMO ocupan el bloque de promociones de Inicio, en el orden
     * en que se muestran. Implementa el criterio de {@link HomeFeaturedPolicy} y es la única
     * definición de "está en portada" del sistema.
     */
    List<Promotion> findHomeFeatured();

    /**
     * Los IDs de {@link #findHomeFeatured()}, para marcar y filtrar filas del panel sin
     * arrastrar las promociones completas. Sale del mismo cálculo, nunca de uno paralelo.
     */
    List<Long> findHomeFeaturedIds();

    /**
     * Una página del catálogo administrativo: filtrada, buscada, ordenada y recortada EN LA
     * BASE DE DATOS. Nunca devuelve más de {@code size} filas, por muchas que haya.
     *
     * @param page índice de página en base 0
     * @param size filas por página (ya acotado por quien llama)
     */
    List<Promotion> findAdminPage(AdminPromotionFilter filter, int page, int size);

    /** Cuántas filas cumplen {@code filter} en total (para la paginación del panel). */
    long countAdminPage(AdminPromotionFilter filter);

    /** Contadores del catálogo completo, ignorando los filtros del listado. */
    PromotionCatalogCounters countCatalog();

    long countActive();

    Optional<Promotion> findPromotionById(Long id);

    Optional<Promotion> findBySlug(String slug);

    Optional<Promotion> findByFacebookPostId(String facebookPostId);

    Promotion save(Promotion promotion);

    void delete(Long id);
}
