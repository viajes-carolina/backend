-- ==============================================================================
-- Flyway Migration: V11__global_search_trigram_indexes.sql
-- Module: Global Search & Fuzzy Query Trigram Scoring
-- Project: Viajes Carolina (PostgreSQL 17)
-- ==============================================================================

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS unaccent;

-- 1. Trigram Indexes for Promotion entity
CREATE INDEX IF NOT EXISTS idx_promotion_title_trgm ON promotion USING gin (title gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_promotion_dest_trgm ON promotion USING gin (destination gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_promotion_summary_trgm ON promotion USING gin (summary gin_trgm_ops);

-- 2. Trigram Indexes for Travel Intention entity
CREATE INDEX IF NOT EXISTS idx_travel_intention_title_trgm ON travel_intention USING gin (title gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_travel_intention_tagline_trgm ON travel_intention USING gin (tagline gin_trgm_ops);

-- 3. Trigram Indexes for Blog Post entity
CREATE INDEX IF NOT EXISTS idx_blog_post_title_trgm ON blog_post USING gin (title gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_blog_post_summary_trgm ON blog_post USING gin (summary gin_trgm_ops);

-- 4. Global Search View (Normalized View for Unified Query Scoring)
CREATE OR REPLACE VIEW v_global_search_index AS
SELECT 
    'PROMOTION' AS entity_type,
    p.id AS entity_id,
    p.slug AS entity_slug,
    p.title AS title,
    p.summary AS subtitle,
    COALESCE(p.destination, '') AS metadata_info,
    COALESCE(m.storage_path, '/media/demo-cartagena-caribe.webp') AS image_url,
    '/promociones' AS target_url,
    p.active AS is_active,
    p.price_usd AS numeric_badge,
    'USD ' || CAST(p.price_usd AS text) AS badge_text
FROM promotion p
LEFT JOIN media_asset m ON p.featured_media_id = m.id
WHERE p.active = TRUE

UNION ALL

SELECT 
    'BLOG_POST' AS entity_type,
    b.id AS entity_id,
    b.slug AS entity_slug,
    b.title AS title,
    b.summary AS subtitle,
    COALESCE(c.name, 'Blog') AS metadata_info,
    COALESCE(m.storage_path, '/media/demo-cartagena-caribe.webp') AS image_url,
    '/blog/' || b.slug AS target_url,
    b.active AS is_active,
    CAST(b.reading_time_minutes AS numeric) AS numeric_badge,
    b.reading_time_minutes || ' min lectura' AS badge_text
FROM blog_post b
LEFT JOIN blog_category c ON b.category_id = c.id
LEFT JOIN media_asset m ON b.cover_media_id = m.id
WHERE b.active = TRUE AND b.status = 'PUBLISHED'

UNION ALL

SELECT 
    'INTENTION' AS entity_type,
    i.id AS entity_id,
    i.slug AS entity_slug,
    i.title AS title,
    i.tagline AS subtitle,
    'Experiencia de Viaje' AS metadata_info,
    COALESCE(m.storage_path, '/media/demo-cartagena-caribe.webp') AS image_url,
    '/#intenciones' AS target_url,
    i.active AS is_active,
    CAST(i.display_order AS numeric) AS numeric_badge,
    i.icon_name AS badge_text
FROM travel_intention i
LEFT JOIN media_asset m ON i.cover_media_id = m.id
WHERE i.active = TRUE;
