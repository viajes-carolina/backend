-- La sección de Experiencias/Testimonios del Home muestra dos fotos (el blob de
-- fondo y la foto Polaroid superpuesta) que hasta ahora reutilizaban una foto
-- secundaria del Hero sin ninguna forma de editarlas directamente desde aquí.
-- Se agregan dos slots independientes, editables por el admin en esta misma
-- sección, siguiendo el mismo patrón ya usado en home_promotions_section (V35).
ALTER TABLE home_testimonials_section
  ADD COLUMN blob_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
  ADD COLUMN blob_media_url VARCHAR(500),
  ADD COLUMN blob_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN blob_focal_y DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN polaroid_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
  ADD COLUMN polaroid_media_url VARCHAR(500),
  ADD COLUMN polaroid_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN polaroid_focal_y DOUBLE PRECISION DEFAULT 50.0;
