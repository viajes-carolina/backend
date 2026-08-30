package com.viajescarolina.api.legal.domain;

/**
 * Categoría del panel de preferencias de cookies (Esenciales, Analítica,
 * Preferencias). "Esenciales" se marca con required = true y no puede
 * desactivarse desde el panel público.
 */
public record CookieCategory(String key, String name, String description, boolean required) {}
