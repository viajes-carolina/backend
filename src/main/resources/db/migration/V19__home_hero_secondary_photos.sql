-- ==============================================================================
-- Migración V19: Fotos secundarias del Home Hero (collage H6) y línea de confianza
-- El Hero pasa de 1 foto de fondo a pantalla completa a 1 foto principal +
-- 2 fotos secundarias en collage (rediseño UX/UI del sitio público).
-- ==============================================================================

ALTER TABLE home_hero
    ADD COLUMN IF NOT EXISTS secondary_media_1_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    ADD COLUMN IF NOT EXISTS secondary_media_1_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS secondary_media_1_focal_x NUMERIC(5,2) DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS secondary_media_1_focal_y NUMERIC(5,2) DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS secondary_media_2_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    ADD COLUMN IF NOT EXISTS secondary_media_2_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS secondary_media_2_focal_x NUMERIC(5,2) DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS secondary_media_2_focal_y NUMERIC(5,2) DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS trust_stat_text VARCHAR(300)
        DEFAULT 'Más de 1,000 viajeros han confiado en nosotros para vivir recuerdos inolvidables.';

-- Sin valores semilla para las fotos secundarias ni fallback a una imagen demo:
-- si el admin todavía no las sube, el frontend muestra un placeholder abstracto
-- (degradado + ícono), nunca una foto de archivo hardcodeada.
