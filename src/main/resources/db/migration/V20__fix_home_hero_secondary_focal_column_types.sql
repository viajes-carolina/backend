-- ==============================================================================
-- V20: Corrige el tipo de columna de los puntos focales secundarios del Hero
-- V19 los creó como NUMERIC(5,2) (copiando la definición original de
-- background_focal_x/y en V4), pero la entidad Java los mapea como Double sin
-- columnDefinition explícita, que Hibernate espera como double precision —
-- el mismo tipo en el que ya vive background_focal_x/y en esta base.
-- ==============================================================================

ALTER TABLE home_hero
    ALTER COLUMN secondary_media_1_focal_x TYPE DOUBLE PRECISION,
    ALTER COLUMN secondary_media_1_focal_y TYPE DOUBLE PRECISION,
    ALTER COLUMN secondary_media_2_focal_x TYPE DOUBLE PRECISION,
    ALTER COLUMN secondary_media_2_focal_y TYPE DOUBLE PRECISION;
