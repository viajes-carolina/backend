package com.viajescarolina.api.legal.domain;

/**
 * Ítem {title, body} de una sección numerada de una página legal. Reutilizado
 * por las 5 páginas del bounded context legal (Términos, Privacidad, Cookies,
 * ESNNA, MINCETUR), cada una con una cantidad variable de secciones.
 */
public record LegalSection(String title, String body) {}
