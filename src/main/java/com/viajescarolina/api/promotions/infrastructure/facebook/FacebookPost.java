package com.viajescarolina.api.promotions.infrastructure.facebook;

/**
 * Representación mínima de un post del feed de una Página de Facebook,
 * tal como lo devuelve la Graph API (ver {@link FacebookGraphClient}).
 */
public record FacebookPost(
        String id,
        String message,
        String createdTime,
        String permalinkUrl,
        String photoUrl
) {}
