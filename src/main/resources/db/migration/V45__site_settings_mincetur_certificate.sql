-- Agrega el enlace a la constancia de registro MINCETUR (Ministerio de Comercio
-- Exterior y Turismo) mostrada en el footer del sitio. Opcional: si no se
-- configura, el frontend no debe mostrar el enlace como si existiera.
ALTER TABLE site_settings
  ADD COLUMN mincetur_certificate_url VARCHAR(500);
