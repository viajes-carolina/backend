-- Revierte la decisión de V38 por pedido de negocio: el autor de un artículo
-- del blog ya no es texto/foto editorial libre — SIEMPRE debe ser una
-- asesora real registrada en travel_advisor (Bounded Context `about`, que
-- ya administra el equipo real en /nosotros/equipo). Se detectaron 2
-- artículos publicados con author_name "Lucía Ramos" y "Valeria Gómez" que
-- no corresponden a personal real; se reasignan a Carolina Zúñiga, única
-- asesora existente hoy, junto con cualquier otro post cuyo autor no
-- coincida con una asesora real.
ALTER TABLE blog_post ADD COLUMN author_advisor_id BIGINT REFERENCES travel_advisor(id);

UPDATE blog_post
SET author_advisor_id = (SELECT id FROM travel_advisor WHERE full_name = 'Carolina Zúñiga' LIMIT 1);

-- Si ningún ambiente tiene una asesora "Carolina Zúñiga" exacta, este NOT
-- NULL fallará de forma visible en vez de dejar posts sin autor válido —
-- comportamiento deseado, no debe corromper datos en silencio.
ALTER TABLE blog_post ALTER COLUMN author_advisor_id SET NOT NULL;

ALTER TABLE blog_post
  DROP COLUMN author_name,
  DROP COLUMN author_avatar_media_id,
  DROP COLUMN author_avatar_focal_x,
  DROP COLUMN author_avatar_focal_y;
