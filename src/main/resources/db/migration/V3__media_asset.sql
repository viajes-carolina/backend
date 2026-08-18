-- ==============================================================================
-- V3__media_asset.sql
-- Bounded Context: Media Library & Asset Management
-- Viajes Carolina — High-Performance Responsive Media with Focal Points
-- ==============================================================================

CREATE TABLE IF NOT EXISTS media_asset (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL UNIQUE,
    original_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    width INT NOT NULL DEFAULT 0,
    height INT NOT NULL DEFAULT 0,
    focal_x NUMERIC(5, 2) NOT NULL DEFAULT 50.00 CHECK (focal_x >= 0 AND focal_x <= 100),
    focal_y NUMERIC(5, 2) NOT NULL DEFAULT 50.00 CHECK (focal_y >= 0 AND focal_y <= 100),
    alt_text VARCHAR(255),
    caption VARCHAR(500),
    storage_path VARCHAR(500) NOT NULL,
    variants_json JSONB DEFAULT '{}'::jsonb,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices estratégicos para filtrado, búsqueda y ordenación
CREATE INDEX IF NOT EXISTS idx_media_asset_active ON media_asset(is_active);
CREATE INDEX IF NOT EXISTS idx_media_asset_mime ON media_asset(mime_type);
CREATE INDEX IF NOT EXISTS idx_media_asset_created ON media_asset(created_at DESC);

-- Trigger para actualización automática de updated_at
CREATE OR REPLACE FUNCTION update_media_asset_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_media_asset_updated_at ON media_asset;
CREATE TRIGGER trg_media_asset_updated_at
    BEFORE UPDATE ON media_asset
    FOR EACH ROW
    EXECUTE FUNCTION update_media_asset_timestamp();

-- Semillas iniciales para destinos y recursos clave
INSERT INTO media_asset (id, filename, original_name, mime_type, file_size_bytes, width, height, focal_x, focal_y, alt_text, caption, storage_path, variants_json, is_active)
VALUES 
(1, 'demo-hero-travel.webp', 'hero-travel-banner.webp', 'image/webp', 245800, 1920, 1080, 50.00, 40.00, 'Paisaje de viaje y aventura en los Andes', 'Vista panorámica de montaña y cielo despejado', '/media/demo-hero-travel.webp', '{"thumb": "/media/demo-hero-travel-thumb.webp", "medium": "/media/demo-hero-travel-med.webp", "large": "/media/demo-hero-travel.webp"}'::jsonb, TRUE),
(2, 'demo-cartagena-caribe.webp', 'cartagena-playa.webp', 'image/webp', 184500, 1200, 800, 60.00, 50.00, 'Calles coloniales y mar de Cartagena de Indias', 'Cartagena de Indias al atardecer', '/media/demo-cartagena-caribe.webp', '{"thumb": "/media/demo-cartagena-thumb.webp", "medium": "/media/demo-cartagena-med.webp"}'::jsonb, TRUE),
(3, 'demo-cusco-machupicchu.webp', 'cusco-valle-sagrado.webp', 'image/webp', 210400, 1200, 800, 50.00, 35.00, 'Santuario Histórico de Machu Picchu y Valle Sagrado', 'Machu Picchu bajo la luz de la mañana', '/media/demo-cusco-machupicchu.webp', '{"thumb": "/media/demo-cusco-thumb.webp", "medium": "/media/demo-cusco-med.webp"}'::jsonb, TRUE),
(4, 'demo-logo-viajes-carolina.webp', 'logo-vc-oficial.webp', 'image/webp', 45200, 600, 200, 50.00, 50.00, 'Logo Oficial de Viajes Carolina', 'Logo corporativo', '/media/demo-logo-viajes-carolina.webp', '{}'::jsonb, TRUE)
ON CONFLICT (id) DO NOTHING;

SELECT setval('media_asset_id_seq', (SELECT COALESCE(MAX(id), 1) FROM media_asset));
