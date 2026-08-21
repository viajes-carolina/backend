-- ==============================================================================
-- Migración V23: baja de "Intenciones de Viaje"
-- La sección desapareció de la narrativa de Inicio (Figma: "Home narrativa
-- · Guía responsive y contenido", node 396:1000 — la secuencia pasó de 7 a
-- 6 pasos). Se da de baja el bounded context completo.
--
-- La vista v_global_search_index (V11) depende de travel_intention, así
-- que hay que redefinirla ANTES del DROP TABLE o Postgres lo rechaza.
-- ==============================================================================

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
WHERE b.active = TRUE AND b.status = 'PUBLISHED';

-- Los índices trigram sobre travel_intention (V11) y el índice de
-- display_order (V5) se borran solos junto con la tabla.
DROP TABLE IF EXISTS travel_intention;
