package com.viajescarolina.api.promotions.infrastructure.facebook;

/**
 * Resultado de publicar una foto con caption en la Página de Facebook de Viajes Carolina
 * (ver {@link FacebookGraphClient#publishPhoto}).
 */
public record FacebookPublishResult(String postId, String permalinkUrl) {}
