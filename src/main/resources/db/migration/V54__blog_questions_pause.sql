-- ==============================================================================
-- V54__blog_questions_pause.sql
-- Viajes Carolina - Configuración editorial del bloque "Cuatro preguntas
-- antes de elegir" de la página pública /blog (bounded context blog).
-- Tabla singleton editable desde el admin, mismo patrón que Home (V25).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS blog_questions_pause (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow_text VARCHAR(255) NOT NULL DEFAULT '02 · NOTAS PARA PREPARAR',
    title VARCHAR(255) NOT NULL DEFAULT 'Cuatro preguntas antes de elegir',
    description TEXT NOT NULL DEFAULT 'Respuestas breves para decidir con más claridad, sin resolverlo todo de una vez.',
    cta_text VARCHAR(255) NOT NULL DEFAULT 'Explorar todas las notas →',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_blog_questions_pause_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO blog_questions_pause (
    id,
    eyebrow_text,
    title,
    description,
    cta_text
) VALUES (
    1,
    '02 · NOTAS PARA PREPARAR',
    'Cuatro preguntas antes de elegir',
    'Respuestas breves para decidir con más claridad, sin resolverlo todo de una vez.',
    'Explorar todas las notas →'
) ON CONFLICT (id) DO NOTHING;
