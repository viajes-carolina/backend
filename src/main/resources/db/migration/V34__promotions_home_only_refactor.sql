-- ==============================================================================
-- V34__promotions_home_only_refactor.sql
-- Bounded Context: Promotions
-- Viajes Carolina — Se elimina por completo la creación/edición manual de
-- promociones desde el panel admin. El único contenido nuevo viene de la
-- sincronización con Facebook Graph API; el admin solo puede mostrar/ocultar
-- (campo active). La selección de las promociones del Home pasa a ser 100%
-- derivada de active + created_at, por lo que is_featured y display_order
-- (mecanismos de "destacar a mano" / "orden manual") dejan de tener sentido.
-- ==============================================================================

-- Índice que referencia is_featured/display_order — debe caer antes que las columnas.
DROP INDEX IF EXISTS idx_promotion_featured;

-- Obsoletos como mecanismo de selección: la selección ahora es 100% derivada
-- (active + created_at), no hay más "destacar a mano" ni "orden manual".
ALTER TABLE promotion
  DROP COLUMN is_featured,
  DROP COLUMN display_order;

-- Índice para la query caliente del Home ("3 activas más recientes") y para
-- el conteo de activas del guard de "nunca bajar de 3".
CREATE INDEX idx_promotion_active_created_at
  ON promotion (created_at DESC, id DESC)
  WHERE active = true;

-- Galería adicional (V32): sin consumidor público y su único editor
-- (el modal de edición manual) desaparece en este refactor.
DROP TABLE IF EXISTS promotion_media;
