-- ==============================================================================
-- V32__promotion_media_gallery.sql
-- Bounded Context: Promotions — Additional Media Gallery
-- Viajes Carolina — Galería de fotos adicionales por promoción
-- (featured_media_id sigue siendo la portada; esta tabla es solo el "extra")
-- ==============================================================================

CREATE TABLE promotion_media (
    id BIGSERIAL PRIMARY KEY,
    promotion_id BIGINT NOT NULL REFERENCES promotion(id) ON DELETE CASCADE,
    media_asset_id BIGINT NOT NULL REFERENCES media_asset(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (promotion_id, media_asset_id)
);

CREATE INDEX idx_promotion_media_promotion ON promotion_media(promotion_id, display_order);
