-- ==============================================================================
-- V28__home_faq_section.sql
-- Viajes Carolina - Configuración Editorial de la Sección de Preguntas
-- Frecuentes en Home. El badge pasa de "05" a "06" porque V25 insertó una
-- nueva sección (Pausa Conversacional) entre Blog/Inspiración y Testimonios.
--
-- Este copy vivía hardcodeado en FaqSection.tsx; se vuelve editable desde el
-- panel admin siguiendo el mismo patrón singleton que home_blog_inspiration
-- (V12).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS home_faq_section (
    id BIGINT PRIMARY KEY DEFAULT 1,
    badge_text VARCHAR(100) NOT NULL DEFAULT '06 · Antes de continuar',
    title VARCHAR(200) NOT NULL DEFAULT 'Lo que solemos conversar antes de viajar',
    subtitle TEXT NOT NULL DEFAULT 'Es normal tener dudas sobre fechas, pagos o destinos. Aquí respondemos las más frecuentes.',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_home_faq_section_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO home_faq_section (
    id,
    badge_text,
    title,
    subtitle
) VALUES (
    1,
    '06 · Antes de continuar',
    'Lo que solemos conversar antes de viajar',
    'Es normal tener dudas sobre fechas, pagos o destinos. Aquí respondemos las más frecuentes.'
) ON CONFLICT (id) DO NOTHING;
