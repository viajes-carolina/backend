-- Portada: se elimina la columna denormalizada (nunca se volvía a escribir
-- tras el seed V10). coverMediaUrl se resuelve en tiempo de lectura vía
-- MediaRepository. El foco es propio de este post (no heredado del foco
-- global de media_asset), mismo criterio que home_testimonials_section (V37).
ALTER TABLE blog_post DROP COLUMN IF EXISTS cover_media_url;
ALTER TABLE blog_post
  ADD COLUMN cover_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN cover_focal_y DOUBLE PRECISION DEFAULT 50.0;

-- Avatar del autor: mismo patrón mediaId + foco propio, sin url guardada.
-- Deliberadamente sin FK a about.TravelAdvisor (otro Bounded Context) — el
-- autor de un post es texto editorial libre, no siempre una asesora activa.
ALTER TABLE blog_post
  ADD COLUMN author_avatar_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
  ADD COLUMN author_avatar_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN author_avatar_focal_y DOUBLE PRECISION DEFAULT 50.0;

-- Índices sin uso real: idx_blog_post_slug / idx_blog_category_slug son
-- redundantes con su propio UNIQUE constraint; idx_blog_post_tags_gin no
-- tiene ninguna query que filtre por tags hoy.
DROP INDEX IF EXISTS idx_blog_post_slug;
DROP INDEX IF EXISTS idx_blog_category_slug;
DROP INDEX IF EXISTS idx_blog_post_tags_gin;

-- Índice parcial que coincide con el patrón de lectura real del sitio
-- público (active=true AND status='PUBLISHED' ORDER BY published_at DESC).
CREATE INDEX IF NOT EXISTS idx_blog_post_public_feed
  ON blog_post (published_at DESC)
  WHERE active = TRUE AND status = 'PUBLISHED';
