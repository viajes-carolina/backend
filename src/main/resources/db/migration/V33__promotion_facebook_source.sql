-- ==============================================================================
-- V33__promotion_facebook_source.sql
-- Bounded Context: Promotions — Facebook Graph API Sync
-- Viajes Carolina — Origen de la promoción (creación manual vs. sincronización
-- automática desde el feed de la Página de Facebook) y trazabilidad del post
-- de origen para evitar duplicados en cada sincronización.
-- ==============================================================================

ALTER TABLE promotion
  ADD COLUMN source VARCHAR(20) NOT NULL DEFAULT 'MANUAL',
  ADD COLUMN facebook_post_id VARCHAR(64) UNIQUE,
  ADD COLUMN facebook_permalink_url VARCHAR(500);

CREATE INDEX idx_promotion_facebook_post_id ON promotion(facebook_post_id)
  WHERE facebook_post_id IS NOT NULL;
