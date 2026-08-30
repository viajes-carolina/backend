-- ==============================================================================
-- V53__blog_editorial_index.sql
-- Viajes Carolina - Configuración editorial del bloque "Índice editorial"
-- de la página pública /blog (bounded context blog). Tabla singleton
-- editable desde el admin, mismo patrón que las secciones de Home (V25).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS blog_editorial_index (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow_text VARCHAR(255) NOT NULL DEFAULT '01 · ELIGE TU PUNTO DE PARTIDA',
    title VARCHAR(255) NOT NULL DEFAULT 'Lee según el momento de tu viaje',
    description TEXT NOT NULL DEFAULT 'Una guía puede inspirarte, resolver una duda o ayudarte a decidir con más claridad.',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_blog_editorial_index_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO blog_editorial_index (
    id,
    eyebrow_text,
    title,
    description
) VALUES (
    1,
    '01 · ELIGE TU PUNTO DE PARTIDA',
    'Lee según el momento de tu viaje',
    'Una guía puede inspirarte, resolver una duda o ayudarte a decidir con más claridad.'
) ON CONFLICT (id) DO NOTHING;
