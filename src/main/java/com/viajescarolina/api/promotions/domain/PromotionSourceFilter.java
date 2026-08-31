package com.viajescarolina.api.promotions.domain;

/**
 * Origen por el que el panel filtra el catálogo de promociones. Se corresponde uno a uno
 * con la columna {@code source}; ausencia de filtro se representa con {@code null}.
 */
public enum PromotionSourceFilter {

    /** Dada de alta desde el formulario del panel. */
    MANUAL,

    /** Legado del ingestor de la Página de Facebook, ya retirado. */
    FACEBOOK
}
