-- ==============================================================================
-- Migración V47: baja de 4 columnas de media huérfanas en home_promotions_section
--
-- Confirmadas como código muerto tras auditoría cruzada con vc-fronted: cero
-- consumidores en el frontend público tras el rediseño de la sección de
-- Promotions (TestimonialsSection ya no reutiliza esta foto). El admin ya no
-- tiene selector de imagen para esta sección y el componente público
-- (PromotionsSection) nunca lee media_id/media_url/media_focal_x/media_focal_y.
--
-- Columnas eliminadas:
--   - media_id / media_url / media_focal_x / media_focal_y.
--
-- Ninguna columna tiene NOT NULL, por lo que no hay pérdida de datos
-- bloqueante. La FK a media_asset (media_id) se elimina sola junto con su
-- columna.
--
-- Referencias Java a estos campos (HomePromotionsSection,
-- HomePromotionsSectionPanacheEntity, HomePromotionsSectionDTO,
-- GetPublicHomePromotionsSectionUseCase, UpdateHomePromotionsSectionUseCase)
-- ya fueron retiradas y el compilado + build (`./gradlew build`) fueron
-- verificados en verde antes de aplicar esta migración. Es seguro ejecutarla.
-- ==============================================================================

ALTER TABLE home_promotions_section
  DROP COLUMN IF EXISTS media_id,
  DROP COLUMN IF EXISTS media_url,
  DROP COLUMN IF EXISTS media_focal_x,
  DROP COLUMN IF EXISTS media_focal_y;
