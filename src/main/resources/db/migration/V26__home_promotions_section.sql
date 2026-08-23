-- ==============================================================================
-- V26__home_promotions_section.sql
-- Viajes Carolina - Configuración Editorial de la Sección de Promociones en
-- Home (encabezado + CTA consolidado de WhatsApp al pie de la sección).
--
-- Este copy vivía hardcodeado en el frontend; se vuelve editable desde el
-- panel admin siguiendo el mismo patrón singleton que home_blog_inspiration
-- (V12).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS home_promotions_section (
    id BIGINT PRIMARY KEY DEFAULT 1,
    badge_text VARCHAR(100) NOT NULL DEFAULT '02 · Viajes para empezar a imaginar',
    title VARCHAR(200) NOT NULL DEFAULT 'Algunas formas de vivir tu próximo viaje',
    subtitle TEXT NOT NULL DEFAULT 'Experiencias que podemos ajustar a tus tiempos, compañía y presupuesto.',
    bottom_cta_question VARCHAR(200) NOT NULL DEFAULT '¿Cuál de estos viajes te gustaría vivir?',
    bottom_cta_whatsapp_text VARCHAR(100) NOT NULL DEFAULT 'Cuéntanos cuál te gustó',
    bottom_cta_whatsapp_message VARCHAR(500) NOT NULL DEFAULT 'Hola Viajes Carolina, me gustaría conversar sobre una de sus promociones.',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_home_promotions_section_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO home_promotions_section (
    id,
    badge_text,
    title,
    subtitle,
    bottom_cta_question,
    bottom_cta_whatsapp_text,
    bottom_cta_whatsapp_message
) VALUES (
    1,
    '02 · Viajes para empezar a imaginar',
    'Algunas formas de vivir tu próximo viaje',
    'Experiencias que podemos ajustar a tus tiempos, compañía y presupuesto.',
    '¿Cuál de estos viajes te gustaría vivir?',
    'Cuéntanos cuál te gustó',
    'Hola Viajes Carolina, me gustaría conversar sobre una de sus promociones.'
) ON CONFLICT (id) DO NOTHING;
