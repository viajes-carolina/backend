-- ==============================================================================
-- V57__rename_blog_editorial_index_to_blog_library.sql
-- Viajes Carolina - Renombra el bloque "Índice editorial" a "Biblioteca
-- unificada" en la página pública /blog (bounded context blog), alineando
-- el modelo con el diseño real de Figma: buscador, filtro de categorías y
-- grilla completa de artículos con paginación. También actualiza el
-- eyebrow del bloque de cierre de "03 ·" a "02 ·" tras la reorganización
-- de secciones.
-- ==============================================================================

ALTER TABLE blog_editorial_index RENAME TO blog_library;

UPDATE blog_library SET
    eyebrow_text = '01 · TODAS LAS HISTORIAS',
    title = 'Explora la bitácora',
    description = 'Busca por tema, filtra por categoría y recorre el archivo a tu ritmo.'
WHERE id = 1;

UPDATE blog_closing SET
    eyebrow_text = '02 · SIGAMOS CONVERSANDO'
WHERE id = 1;
