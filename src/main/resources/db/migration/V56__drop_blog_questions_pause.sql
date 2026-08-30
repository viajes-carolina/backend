-- ==============================================================================
-- V56__drop_blog_questions_pause.sql
-- Viajes Carolina - Eliminación de la tabla blog_questions_pause.
-- El bloque "Cuatro preguntas antes de elegir" fue retirado de la página
-- pública /blog por decisión de negocio: no guarda relación con el
-- contenido del blog y no se usará en ningún otro lugar. Se elimina la
-- tabla creada en V54 junto con toda la infraestructura de código asociada.
-- ==============================================================================

DROP TABLE IF EXISTS blog_questions_pause;
