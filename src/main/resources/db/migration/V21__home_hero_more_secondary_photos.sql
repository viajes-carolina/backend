-- ==============================================================================
-- V21: Dos fotos secundarias más para el collage del Home Hero (de 3 a 5 fotos
-- totales) — feedback del usuario: más fotos comunican mejor "hemos
-- acompañado varios viajes". Columnas focales en DOUBLE PRECISION directo
-- (no NUMERIC) para no repetir el drift corregido en V20.
-- ==============================================================================

ALTER TABLE home_hero
    ADD COLUMN IF NOT EXISTS secondary_media_3_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    ADD COLUMN IF NOT EXISTS secondary_media_3_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS secondary_media_3_focal_x DOUBLE PRECISION DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS secondary_media_3_focal_y DOUBLE PRECISION DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS secondary_media_4_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    ADD COLUMN IF NOT EXISTS secondary_media_4_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS secondary_media_4_focal_x DOUBLE PRECISION DEFAULT 50.0,
    ADD COLUMN IF NOT EXISTS secondary_media_4_focal_y DOUBLE PRECISION DEFAULT 50.0;
