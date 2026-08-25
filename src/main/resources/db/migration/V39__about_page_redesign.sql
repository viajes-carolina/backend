-- ==============================================================================
-- Viajes Carolina — Migration V39: Rediseño de "Nosotros" (Figma 549:4 / 549:6)
-- ==============================================================================
-- Reemplaza el Hero de estadísticas + Misión/Visión por el diseño nuevo: foto
-- real con foco propio, tarjetas flotantes de cita, ruta "de idea a recuerdo",
-- y 3 secciones nuevas ("Cómo te acompañamos", "Experiencias que humanizan",
-- "Una persona al otro lado"). Las asesoras ganan una cita personal para el
-- layout editorial de una sola persona.

-- Limpieza: campos que no aparecen en el diseño nuevo de Figma
ALTER TABLE about_page DROP COLUMN IF EXISTS vision_title;
ALTER TABLE about_page DROP COLUMN IF EXISTS vision_body;
ALTER TABLE about_page DROP COLUMN IF EXISTS experience_years;
ALTER TABLE about_page DROP COLUMN IF EXISTS happy_travelers;
ALTER TABLE about_page DROP COLUMN IF EXISTS destinations_count;
ALTER TABLE about_page DROP COLUMN IF EXISTS satisfaction_rate_percent;

-- Hero: foco propio + tarjetas flotantes de cita
ALTER TABLE about_page
  ADD COLUMN hero_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN hero_focal_y DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN hero_card_badge VARCHAR(160),
  ADD COLUMN hero_card_title VARCHAR(255),
  ADD COLUMN hero_note_text VARCHAR(255);

-- Historia: foco propio (antes solo tenía mediaId/url)
ALTER TABLE about_page
  ADD COLUMN story_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN story_focal_y DOUBLE PRECISION DEFAULT 50.0;

-- Misión: cita editorial + ruta "de idea a recuerdo" (4 pasos)
ALTER TABLE about_page
  ADD COLUMN mission_quote TEXT,
  ADD COLUMN journey_steps_json JSONB NOT NULL DEFAULT '[]';

-- Nueva sección "Cómo te acompañamos"
ALTER TABLE about_page
  ADD COLUMN accompany_badge VARCHAR(160),
  ADD COLUMN accompany_title VARCHAR(255),
  ADD COLUMN accompany_subtitle TEXT,
  ADD COLUMN accompany_steps_json JSONB NOT NULL DEFAULT '[]',
  ADD COLUMN accompany_quote TEXT,
  ADD COLUMN accompany_quote_attribution VARCHAR(255);

-- Nueva sección "Experiencias que humanizan"
ALTER TABLE about_page
  ADD COLUMN moments_badge VARCHAR(160),
  ADD COLUMN moments_title VARCHAR(255),
  ADD COLUMN moments_subtitle TEXT,
  ADD COLUMN moments_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
  ADD COLUMN moments_focal_x DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN moments_focal_y DOUBLE PRECISION DEFAULT 50.0,
  ADD COLUMN moments_json JSONB NOT NULL DEFAULT '[]';

-- Nueva sección "Una persona al otro lado" (solo cabecera; las burbujas de
-- conversación quedan fijas en el frontend, no son contenido administrable)
ALTER TABLE about_page
  ADD COLUMN human_badge VARCHAR(160),
  ADD COLUMN human_title VARCHAR(255),
  ADD COLUMN human_subtitle TEXT,
  ADD COLUMN human_tagline VARCHAR(255);

-- Asesoras: cita personal (para el layout editorial de 1 sola asesora)
ALTER TABLE travel_advisor ADD COLUMN quote TEXT;

-- ==============================================================================
-- Seed: copy real del Figma para los campos nuevos del singleton about_page
-- ==============================================================================
UPDATE about_page SET
  hero_card_badge = 'LO PRIMERO ES ESCUCHAR',
  hero_card_title = 'No necesitas tener el destino decidido para empezar.',
  hero_note_text = 'Seguimos contigo hasta el regreso.',
  mission_quote = 'Por eso empezamos escuchando: una buena recomendación no se impone, se construye contigo.',
  journey_steps_json = '[{"label":"Una idea"},{"label":"Decisiones claras"},{"label":"Un viaje a tu ritmo"},{"label":"Un recuerdo propio"}]'::jsonb,
  accompany_badge = '03 · CÓMO TE ACOMPAÑAMOS',
  accompany_title = 'Acompañarte no es darte un itinerario y desaparecer.',
  accompany_subtitle = 'Es permanecer disponible cuando aparecen dudas, cambios o nuevas ideas.',
  accompany_steps_json = '[{"title":"Escucharte","body":"Partimos de tu ritmo, tus prioridades y lo que realmente quieres vivir."},{"title":"Dar forma contigo","body":"Ordenamos destinos, tiempos y decisiones para que el plan se sienta posible."},{"title":"Permanecer presente","body":"Si algo cambia, sabes que hay una persona al otro lado del mensaje."}]'::jsonb,
  accompany_quote = 'No queremos que sientas que compraste un viaje. Queremos que sientas que alguien lo pensó contigo.',
  accompany_quote_attribution = '— La forma Viajes Carolina',
  moments_badge = '04 · LO QUE QUEDA DEL VIAJE',
  moments_title = 'Una agencia también se conoce por lo que sus viajeros recuerdan.',
  moments_subtitle = 'Estas escenas representan historias reales que luego podrán mostrarse con las fotografías de clientes administradas desde el panel.',
  moments_json = '[{"title":"Una familia que necesitaba ir sin prisa","body":"El viaje se diseñó pensando en pausas, compañía y tiempo para disfrutar juntos."},{"title":"Una pareja que aún no tenía destino","body":"La conversación empezó por lo que querían sentir, no por una lista de lugares."},{"title":"Un grupo que quería sentirse acompañado","body":"Cada decisión quedó clara y siempre supieron dónde escribir si algo cambiaba."}]'::jsonb,
  human_badge = '05 · QUIÉN TE ACOMPAÑA',
  human_title = 'Al otro lado no hay respuestas automáticas.',
  human_subtitle = 'Hay una persona que lee tu mensaje, entiende el contexto y piensa contigo el siguiente paso.',
  human_tagline = 'TE LEE · TE ORIENTA · PERMANECE'
WHERE id = 1;

UPDATE travel_advisor
SET quote = 'Quiero que cada persona sienta que puede preguntar, decidir con calma y disfrutar desde antes de viajar.'
WHERE full_name = 'Carolina Zúñiga';
