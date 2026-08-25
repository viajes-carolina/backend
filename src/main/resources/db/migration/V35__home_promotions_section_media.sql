-- ==============================================================================
-- V35__home_promotions_section_media.sql
-- Viajes Carolina - Foto de fondo configurable para el blob decorativo de la
-- sección de Promociones en Home.
--
-- Hasta ahora esa foto se tomaba automáticamente de la promoción más
-- reciente (Promotion.featuredMediaUrl); pasa a ser un slot de media
-- independiente que el admin configura directamente, igual que ya funciona
-- la foto principal del Hero (home_hero.background_media_*).
--
-- Nota: los focales usan DOUBLE PRECISION (no NUMERIC) porque la entidad
-- Java mapea Double sin columnDefinition explícita, que Hibernate espera
-- como double precision. Usar NUMERIC aquí repetiría el error corregido en
-- V20__fix_home_hero_secondary_focal_column_types.sql.
-- ==============================================================================

ALTER TABLE home_promotions_section
  ADD COLUMN media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
  ADD COLUMN media_url VARCHAR(500),
  ADD COLUMN media_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN media_focal_y DOUBLE PRECISION DEFAULT 50.0;
