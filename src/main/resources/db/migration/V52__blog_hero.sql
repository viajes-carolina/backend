-- ==============================================================================
-- V52__blog_hero.sql
-- Viajes Carolina - Configuración editorial del Hero de la página pública
-- /blog (bounded context blog). Tabla singleton editable desde el admin,
-- siguiendo el mismo patrón que las secciones singleton de Home (V25).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS blog_hero (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow_text VARCHAR(255) NOT NULL DEFAULT 'BITÁCORA · VIAJES CAROLINA',
    title VARCHAR(255) NOT NULL DEFAULT 'El diario de Viajes Carolina',
    description TEXT NOT NULL DEFAULT 'Guías claras, ideas y respuestas para preparar el viaje con más confianza y menos ruido.',
    edition_label VARCHAR(255) NOT NULL DEFAULT 'EDICIÓN 01 · AGOSTO 2026',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_blog_hero_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO blog_hero (
    id,
    eyebrow_text,
    title,
    description,
    edition_label
) VALUES (
    1,
    'BITÁCORA · VIAJES CAROLINA',
    'El diario de Viajes Carolina',
    'Guías claras, ideas y respuestas para preparar el viaje con más confianza y menos ruido.',
    'EDICIÓN 01 · AGOSTO 2026'
) ON CONFLICT (id) DO NOTHING;
