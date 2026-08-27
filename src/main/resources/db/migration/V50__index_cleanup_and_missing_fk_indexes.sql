-- ==============================================================================
-- V50__index_cleanup_and_missing_fk_indexes.sql
-- Viajes Carolina — Hallazgos de auditoría de performance (confirmados con
-- EXPLAIN ANALYZE contra la base viva).
-- ==============================================================================

-- DB-XXX: blog_post.author_advisor_id (FK agregada en V49) no tenía índice.
-- Se usa en el WHERE de countByAuthorAdvisorId, invocado en cada intento de
-- borrar una asesora (about.DeleteAdvisorUseCase) — sin índice, cada intento
-- de borrado hace un Seq Scan completo de blog_post.
CREATE INDEX IF NOT EXISTS idx_blog_post_author_advisor_id ON blog_post (author_advisor_id);

-- Índices duplicados/redundantes con su propio UNIQUE constraint (mismo criterio
-- ya aplicado en V38 para blog_post.slug / blog_category.slug). Verificado antes
-- de este DROP que el UNIQUE constraint subyacente existe para cada columna:
--   - admin_user.username / admin_user.email: "NOT NULL UNIQUE" en V14.
--   - claim_record.claim_code: "NOT NULL UNIQUE" en V13.
--   - promotion.slug: "UNIQUE NOT NULL" en V6.
-- Postgres ya crea un índice B-Tree implícito para cada UNIQUE constraint;
-- estos índices manuales son un duplicado exacto que solo agrega overhead de
-- escritura sin ningún beneficio de lectura adicional.
DROP INDEX IF EXISTS idx_admin_user_email;
DROP INDEX IF EXISTS idx_admin_user_username;
DROP INDEX IF EXISTS idx_claim_record_code;
DROP INDEX IF EXISTS idx_promotion_slug;

-- Índices faltantes en FK hacia media_asset en tablas que sí crecen (blog_post,
-- promotion, testimonial). Las FK a media_asset en tablas singleton de 1 fila
-- (about_page, home_hero, site_settings, etc.) no se indexan: el beneficio de
-- un índice sobre una tabla de una sola fila es nulo.
CREATE INDEX IF NOT EXISTS idx_blog_post_cover_media_id ON blog_post (cover_media_id);
CREATE INDEX IF NOT EXISTS idx_promotion_featured_media_id ON promotion (featured_media_id);
CREATE INDEX IF NOT EXISTS idx_testimonial_avatar_media_id ON testimonial (avatar_media_id);
