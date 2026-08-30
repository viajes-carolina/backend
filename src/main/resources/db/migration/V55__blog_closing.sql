-- ==============================================================================
-- V55__blog_closing.sql
-- Viajes Carolina - Configuración editorial del bloque de cierre
-- "¿Encontraste lo que necesitabas?" de la página pública /blog
-- (bounded context blog). Tabla singleton editable desde el admin,
-- mismo patrón que Home (V25).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS blog_closing (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow_text VARCHAR(255) NOT NULL DEFAULT '03 · SIGAMOS CONVERSANDO',
    title VARCHAR(255) NOT NULL DEFAULT '¿Encontraste lo que necesitabas?',
    subtitle TEXT NOT NULL DEFAULT 'Si todavía tienes dudas, Carolina puede ayudarte a ordenar el siguiente paso.',
    whatsapp_cta_text VARCHAR(255) NOT NULL DEFAULT 'Conversemos por WhatsApp',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_blog_closing_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO blog_closing (
    id,
    eyebrow_text,
    title,
    subtitle,
    whatsapp_cta_text
) VALUES (
    1,
    '03 · SIGAMOS CONVERSANDO',
    '¿Encontraste lo que necesitabas?',
    'Si todavía tienes dudas, Carolina puede ayudarte a ordenar el siguiente paso.',
    'Conversemos por WhatsApp'
) ON CONFLICT (id) DO NOTHING;
