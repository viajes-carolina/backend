-- ==============================================================================
-- V27__home_testimonials_section.sql
-- Viajes Carolina - Configuración Editorial de la Sección de Testimonios en
-- Home. El badge pasa de "04" a "05" porque V25 insertó una nueva sección
-- (Pausa Conversacional) entre Blog/Inspiración y Testimonios.
--
-- Este copy vivía hardcodeado en TestimonialsSection.tsx; se vuelve editable
-- desde el panel admin siguiendo el mismo patrón singleton que
-- home_blog_inspiration (V12).
-- ==============================================================================

CREATE TABLE IF NOT EXISTS home_testimonials_section (
    id BIGINT PRIMARY KEY DEFAULT 1,
    badge_text VARCHAR(100) NOT NULL DEFAULT '05 · Historias reales',
    title VARCHAR(200) NOT NULL DEFAULT 'Viajes que hoy se recuerdan así',
    subtitle TEXT NOT NULL DEFAULT 'Cada fotografía guarda una experiencia que comenzó con una conversación.',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_home_testimonials_section_singleton CHECK (id = 1)
);

-- Insertar registro singleton inicial
INSERT INTO home_testimonials_section (
    id,
    badge_text,
    title,
    subtitle
) VALUES (
    1,
    '05 · Historias reales',
    'Viajes que hoy se recuerdan así',
    'Cada fotografía guarda una experiencia que comenzó con una conversación.'
) ON CONFLICT (id) DO NOTHING;
