-- ==============================================================================
-- V25__home_conversational_pause_section.sql
-- Viajes Carolina - Configuración Editorial de la Nueva Sección "Pausa
-- Conversacional" en Home (entre Blog/Inspiración y Testimonios).
--
-- Bloque simple de una sola pregunta editorial + un botón de WhatsApp,
-- siguiendo el mismo patrón singleton que home_blog_inspiration (V12).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS home_conversational_pause (
    id BIGINT PRIMARY KEY DEFAULT 1,
    badge_text VARCHAR(100) NOT NULL DEFAULT '04 · ANTES DE SEGUIR',
    title VARCHAR(200) NOT NULL DEFAULT '¿Ya imaginas cómo podría sentirse tu próximo viaje?',
    subtitle TEXT NOT NULL DEFAULT 'No necesitas tener todo decidido. Cuéntanos qué te ilusiona y una asesora te ayuda a darle forma.',
    whatsapp_cta_text VARCHAR(100) NOT NULL DEFAULT 'Conversarlo por WhatsApp',
    whatsapp_message_template VARCHAR(500) NOT NULL DEFAULT 'Hola Viajes Carolina, quiero contarles qué tengo en mente para mi próximo viaje.',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_home_conversational_pause_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO home_conversational_pause (
    id,
    badge_text,
    title,
    subtitle,
    whatsapp_cta_text,
    whatsapp_message_template
) VALUES (
    1,
    '04 · ANTES DE SEGUIR',
    '¿Ya imaginas cómo podría sentirse tu próximo viaje?',
    'No necesitas tener todo decidido. Cuéntanos qué te ilusiona y una asesora te ayuda a darle forma.',
    'Conversarlo por WhatsApp',
    'Hola Viajes Carolina, quiero contarles qué tengo en mente para mi próximo viaje.'
) ON CONFLICT (id) DO NOTHING;
