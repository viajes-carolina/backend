package com.viajescarolina.api.promotions.domain;

/**
 * Estado por el que el panel filtra el catálogo de promociones.
 *
 * <p>No es una columna: se traduce a una condición sobre {@code active} o sobre
 * {@code valid_until}. Ausencia de filtro (parámetro vacío) se representa con {@code null},
 * nunca con un valor de este enum.</p>
 */
public enum PromotionStatusFilter {

    /** Se está mostrando al público: {@code active = true}. */
    VISIBLE,

    /** Existe pero no se muestra: {@code active = false}. */
    OCULTA,

    /**
     * Su vigencia ya pasó: {@code valid_until < CURRENT_DATE}.
     *
     * <p>Es independiente de {@code active}: una promoción vencida puede seguir visible
     * (justo lo que este filtro sirve para encontrar y corregir).</p>
     */
    VENCIDA
}
