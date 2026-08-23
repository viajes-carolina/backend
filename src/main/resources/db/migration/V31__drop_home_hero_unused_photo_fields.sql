-- ==============================================================================
-- Migración V31: baja de 11 columnas huérfanas de home_hero
--
-- Confirmadas como código muerto tras auditoría cruzada con vc-fronted
-- (apps/web y packages/ui): cero consumidores en el frontend público. El
-- diseño Figma validado del Hero solo usa 3 fotos secundarias, nunca una 4ta
-- ni la "featured card".
--
-- Columnas eliminadas:
--   - secondary_media_4_id / _url / _focal_x / _focal_y (4ta foto secundaria,
--     nunca renderizada; el Hero solo pinta secondary_media_1/2/3).
--   - featured_card_badge / _title / _subtitle / _price_pen / _origin /
--     _media_id / _media_url (familia completa "featured card", sin ningún
--     consumidor en el sitio público).
--
-- Ninguna columna tiene NOT NULL, por lo que no hay pérdida de datos
-- bloqueante. Las FK a media_asset (secondary_media_4_id, featured_card_media_id)
-- se eliminan solas junto con sus columnas.
--
-- Referencias Java a estos campos (HomeHero, HomeHeroPanacheEntity, HomeHeroDTO,
-- UpdateHomeHeroRequest, UpdateHomeHeroUseCase, GetPublicHomeHeroUseCase) ya
-- fueron retiradas y el compilado + build (`./gradlew build`) fueron
-- verificados en verde antes de aplicar esta migración. Es seguro ejecutarla.
-- ==============================================================================

ALTER TABLE home_hero
  DROP COLUMN IF EXISTS secondary_media_4_id,
  DROP COLUMN IF EXISTS secondary_media_4_url,
  DROP COLUMN IF EXISTS secondary_media_4_focal_x,
  DROP COLUMN IF EXISTS secondary_media_4_focal_y,
  DROP COLUMN IF EXISTS featured_card_badge,
  DROP COLUMN IF EXISTS featured_card_title,
  DROP COLUMN IF EXISTS featured_card_subtitle,
  DROP COLUMN IF EXISTS featured_card_price_pen,
  DROP COLUMN IF EXISTS featured_card_origin,
  DROP COLUMN IF EXISTS featured_card_media_id,
  DROP COLUMN IF EXISTS featured_card_media_url;
