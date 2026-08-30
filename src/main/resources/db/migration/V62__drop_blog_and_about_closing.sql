-- ==============================================================================
-- V62__drop_blog_and_about_closing.sql
-- Viajes Carolina - Eliminación del bloque de cierre "conversemos" en Blog
-- y Nosotros. Home mantiene su propio cierre (no se toca). Se elimina por
-- completo la tabla blog_closing (eyebrow+título+subtítulo+CTA WhatsApp) y
-- las 4 columnas closing_* de about_page, junto con toda la infraestructura
-- de código asociada en los bounded contexts blog y about.
-- ==============================================================================

DROP TABLE IF EXISTS blog_closing;

ALTER TABLE about_page DROP COLUMN IF EXISTS closing_eyebrow;
ALTER TABLE about_page DROP COLUMN IF EXISTS closing_title;
ALTER TABLE about_page DROP COLUMN IF EXISTS closing_subtitle;
ALTER TABLE about_page DROP COLUMN IF EXISTS closing_whatsapp_cta_text;
