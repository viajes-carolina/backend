-- Las promociones sincronizadas por lectura (source=FACEBOOK) no encajan en el nuevo
-- modelo (contenido admin-autorado, publicado hacia Facebook, no al revés) y eran la
-- causa raíz de un problema visual (texto libre de Facebook sin formato controlado
-- rompía el diseño de las tarjetas del sitio). Se desactivan, no se borran, por si se
-- quiere revisar el historial.
UPDATE promotion
SET active = false, updated_at = now()
WHERE source = 'FACEBOOK' AND active = true;
