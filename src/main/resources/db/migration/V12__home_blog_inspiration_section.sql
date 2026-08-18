-- ============================================================================
-- V12__home_blog_inspiration_section.sql
-- Viajes Carolina - Configuración Editorial de la Sección de Inspiración en Home
-- ============================================================================

CREATE TABLE IF NOT EXISTS home_blog_inspiration (
    id BIGINT PRIMARY KEY DEFAULT 1,
    badge_text VARCHAR(100) NOT NULL DEFAULT 'Inspiración para tu viaje',
    title_highlight VARCHAR(200) NOT NULL DEFAULT 'Consejos y guías',
    title_accent VARCHAR(200) NOT NULL DEFAULT 'para explorar el mundo',
    subtitle TEXT NOT NULL DEFAULT 'Descubre recomendaciones de viaje, mejores temporadas, qué empacar y secretos locales de la mano de nuestras asesoras expertas.',
    cta_text VARCHAR(100) NOT NULL DEFAULT 'Ver todos los artículos del blog',
    cta_url VARCHAR(255) NOT NULL DEFAULT '/blog',
    posts_limit INT NOT NULL DEFAULT 3,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_home_blog_inspiration_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO home_blog_inspiration (
    id,
    badge_text,
    title_highlight,
    title_accent,
    subtitle,
    cta_text,
    cta_url,
    posts_limit,
    active
) VALUES (
    1,
    'Inspiración para tu viaje',
    'Consejos y guías',
    'para explorar el mundo',
    'Descubre recomendaciones de viaje, mejores temporadas, qué empacar y secretos locales de la mano de nuestras asesoras expertas.',
    'Ver todos los artículos del blog',
    '/blog',
    3,
    TRUE
) ON CONFLICT (id) DO NOTHING;
