-- ==============================================================================
-- Migración V48: baja de 4 columnas "polaroid" huérfanas en
-- home_testimonials_section
--
-- Confirmadas como código muerto tras auditoría cruzada con vc-fronted: cero
-- consumidores en el frontend público. El componente público
-- TestimonialsSection.tsx solo consume PhotoBlob (columnas blob_media_id /
-- blob_media_url / blob_focal_x / blob_focal_y, que SÍ están vivas y no se
-- tocan en esta migración); la foto Polaroid superpuesta nunca fue conectada
-- a ningún consumidor real.
--
-- Columnas eliminadas:
--   - polaroid_media_id / polaroid_media_url / polaroid_focal_x /
--     polaroid_focal_y.
--
-- Ninguna columna tiene NOT NULL, por lo que no hay pérdida de datos
-- bloqueante. La FK a media_asset (polaroid_media_id) se elimina sola junto
-- con su columna.
--
-- Referencias Java a estos campos (HomeTestimonialsSection,
-- HomeTestimonialsSectionPanacheEntity, HomeTestimonialsSectionDTO,
-- GetPublicHomeTestimonialsSectionUseCase, UpdateHomeTestimonialsSectionUseCase)
-- ya fueron retiradas y el compilado + build (`./gradlew build`) fueron
-- verificados en verde antes de aplicar esta migración. Es seguro ejecutarla.
-- ==============================================================================

ALTER TABLE home_testimonials_section
  DROP COLUMN IF EXISTS polaroid_media_id,
  DROP COLUMN IF EXISTS polaroid_media_url,
  DROP COLUMN IF EXISTS polaroid_focal_x,
  DROP COLUMN IF EXISTS polaroid_focal_y;
