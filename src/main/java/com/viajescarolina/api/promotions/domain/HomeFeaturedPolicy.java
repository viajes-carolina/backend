package com.viajescarolina.api.promotions.domain;

/**
 * Regla de negocio de "estar en portada", en un único sitio.
 *
 * <p>Una promoción está en portada si es una de las {@link #SLOTS} promociones
 * <b>activas más recientes</b> por {@code (createdAt DESC, id DESC)}. No existe columna que
 * lo diga: {@code is_featured} y {@code display_order} se eliminaron en la migración V34
 * precisamente porque la portada dejó de curarse a mano y pasó a derivarse de la actividad
 * y la fecha de alta.</p>
 *
 * <p>Como es un dato derivado, tres consumidores distintos necesitan la MISMA respuesta:</p>
 * <ul>
 *   <li>el bloque de Inicio ({@code ListFeaturedPromotionsUseCase} / endpoint público),</li>
 *   <li>la marca {@code featuredInHome} de cada fila del listado del panel,</li>
 *   <li>el filtro {@code featured=SI|NO} de ese mismo listado.</li>
 * </ul>
 *
 * <p>Si cada uno reimplementara el criterio, bastaría con que uno cambiara de orden o de
 * límite para que el panel marcara "en portada" filas que Inicio no muestra. Por eso el
 * criterio completo (activa + orden + cuántas caben) vive aquí y en
 * {@link PromotionRepository#findHomeFeatured()} / {@link PromotionRepository#findHomeFeaturedIds()},
 * y nadie más lo reescribe.</p>
 */
public final class HomeFeaturedPolicy {

    /** Huecos del bloque de promociones de Inicio. */
    public static final int SLOTS = 3;

    private HomeFeaturedPolicy() {
    }
}
