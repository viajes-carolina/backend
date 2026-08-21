-- ==============================================================================
-- Migración V22: Texto "eyebrow" del Home Hero (etiqueta corta sobre el título)
-- Alineación con la propuesta de Figma "Header integrado" (node 266:231).
-- ==============================================================================

ALTER TABLE home_hero
    ADD COLUMN IF NOT EXISTS eyebrow_text VARCHAR(120)
        DEFAULT 'Empieza con una conversación';
