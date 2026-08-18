-- ==============================================================================
-- Migración V4: Tabla Singleton home_hero y Relación con media_asset (Corte 4)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS home_hero (
    id INT PRIMARY KEY DEFAULT 1 CHECK (id = 1),
    badge_text VARCHAR(100) NOT NULL DEFAULT 'Empieza con una conversación',
    title_highlight VARCHAR(255) NOT NULL DEFAULT 'Tu viaje comienza',
    title_accent VARCHAR(255) NOT NULL DEFAULT 'antes de despegar',
    description TEXT NOT NULL DEFAULT 'Desde la primera idea hasta tu regreso, una asesora te acompaña con opciones claras, atención humana y respaldo en cada etapa.',
    whatsapp_cta_text VARCHAR(100) NOT NULL DEFAULT 'Cuéntame tu viaje',
    whatsapp_message_override VARCHAR(500) DEFAULT 'Hola Viajes Carolina, quiero empezar a planear mi próximo viaje.',
    secondary_cta_text VARCHAR(100) DEFAULT 'Explorar promociones',
    secondary_cta_url VARCHAR(255) DEFAULT '#promociones',
    trust_indicators JSONB NOT NULL DEFAULT '["Asesoría sin costo", "Respuesta rápida", "Acompañamiento real"]'::jsonb,
    background_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    featured_card_badge VARCHAR(100) DEFAULT 'Próxima Parada · Cusco',
    featured_card_title VARCHAR(255) DEFAULT 'Machu Picchu & Valle Sagrado',
    featured_card_subtitle VARCHAR(255) DEFAULT 'Experiencia personalizada de 5 días / 4 noches',
    featured_card_price_pen NUMERIC(10,2) DEFAULT 1922.00,
    featured_card_origin VARCHAR(100) DEFAULT 'Desde Lima',
    featured_card_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Semilla singleton inicial para home_hero
INSERT INTO home_hero (
    id,
    badge_text,
    title_highlight,
    title_accent,
    description,
    whatsapp_cta_text,
    whatsapp_message_override,
    secondary_cta_text,
    secondary_cta_url,
    trust_indicators,
    background_media_id,
    featured_card_badge,
    featured_card_title,
    featured_card_subtitle,
    featured_card_price_pen,
    featured_card_origin,
    featured_card_media_id,
    revision
) VALUES (
    1,
    'Empieza con una conversación',
    'Tu viaje comienza',
    'antes de despegar',
    'Desde la primera idea hasta tu regreso, una asesora te acompaña con opciones claras, atención humana y respaldo en cada etapa.',
    'Cuéntame tu viaje',
    'Hola Viajes Carolina, quiero empezar a planear mi próximo viaje.',
    'Explorar promociones',
    '#promociones',
    '["Asesoría sin costo", "Respuesta rápida", "Acompañamiento real"]'::jsonb,
    1,
    'Próxima Parada · Cusco',
    'Machu Picchu & Valle Sagrado',
    'Experiencia personalizada de 5 días / 4 noches',
    1922.00,
    'Desde Lima',
    3,
    1
) ON CONFLICT (id) DO NOTHING;
