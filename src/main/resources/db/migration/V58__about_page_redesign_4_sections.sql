-- ==============================================================================
-- Viajes Carolina — Migration V58: Rediseño de "Nosotros" a 4 secciones (Figma)
-- ==============================================================================
-- La página pública /nosotros pasa de 6 a 4 secciones: Hero (01), Nuestra
-- forma de trabajar (02), Quién está detrás (03) y Hablemos (04). Se eliminan
-- Historia (huérfana, nunca se renderizó en el público), Misión, Experiencias
-- que humanizan y Una persona al otro lado. El Hero deja de llevar foto de
-- fondo. Las asesoras (tabla travel_advisor) no se tocan.

-- ------------------------------------------------------------------------------
-- 1. Limpieza: columnas de secciones eliminadas
-- ------------------------------------------------------------------------------

-- Historia (huérfana, nunca se renderizó en el público)
ALTER TABLE about_page DROP COLUMN IF EXISTS story_title;
ALTER TABLE about_page DROP COLUMN IF EXISTS story_body;
ALTER TABLE about_page DROP COLUMN IF EXISTS story_media_id;
ALTER TABLE about_page DROP COLUMN IF EXISTS story_focal_x;
ALTER TABLE about_page DROP COLUMN IF EXISTS story_focal_y;
ALTER TABLE about_page DROP COLUMN IF EXISTS values_json;

-- Misión
ALTER TABLE about_page DROP COLUMN IF EXISTS mission_title;
ALTER TABLE about_page DROP COLUMN IF EXISTS mission_body;
ALTER TABLE about_page DROP COLUMN IF EXISTS mission_quote;
ALTER TABLE about_page DROP COLUMN IF EXISTS journey_steps_json;

-- Experiencias que humanizan
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_badge;
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_title;
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_subtitle;
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_media_id;
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_focal_x;
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_focal_y;
ALTER TABLE about_page DROP COLUMN IF EXISTS moments_json;

-- Una persona al otro lado
ALTER TABLE about_page DROP COLUMN IF EXISTS human_badge;
ALTER TABLE about_page DROP COLUMN IF EXISTS human_title;
ALTER TABLE about_page DROP COLUMN IF EXISTS human_subtitle;
ALTER TABLE about_page DROP COLUMN IF EXISTS human_tagline;

-- Hero: el nuevo diseño no lleva foto de fondo
ALTER TABLE about_page DROP COLUMN IF EXISTS hero_media_id;
ALTER TABLE about_page DROP COLUMN IF EXISTS hero_focal_x;
ALTER TABLE about_page DROP COLUMN IF EXISTS hero_focal_y;

-- Acompañamiento: la atribución de la cita ya no existe en el diseño nuevo
ALTER TABLE about_page DROP COLUMN IF EXISTS accompany_quote_attribution;

-- ------------------------------------------------------------------------------
-- 2. Columnas nuevas (con defaults que siembran automáticamente la fila id=1)
-- ------------------------------------------------------------------------------

ALTER TABLE about_page
  ADD COLUMN hero_card_location VARCHAR(150) NOT NULL DEFAULT 'Lima, Perú',
  ADD COLUMN hero_card_detail TEXT NOT NULL DEFAULT 'Atención directa con Carolina
Asesoría · Organización · Seguimiento',
  ADD COLUMN advisors_badge VARCHAR(160) NOT NULL DEFAULT '03 · QUIÉN ESTÁ DETRÁS',
  ADD COLUMN advisors_highlights_json JSONB NOT NULL DEFAULT '[]',
  ADD COLUMN closing_eyebrow VARCHAR(160) NOT NULL DEFAULT '04 · HABLEMOS',
  ADD COLUMN closing_title VARCHAR(255) NOT NULL DEFAULT 'Tu viaje puede empezar con una conversación.',
  ADD COLUMN closing_subtitle TEXT NOT NULL DEFAULT 'Cuéntanos tu idea. Carolina te responderá personalmente.',
  ADD COLUMN closing_whatsapp_cta_text VARCHAR(100) NOT NULL DEFAULT 'Conversemos por WhatsApp';

-- ------------------------------------------------------------------------------
-- 3. Seed: copy real del Figma para las columnas que sobreviven de la fila id=1
-- ------------------------------------------------------------------------------

UPDATE about_page SET
  hero_badge = 'NOSOTROS · VIAJES CAROLINA',
  hero_title = 'Una agencia cercana. Un viaje bien acompañado.',
  hero_subtitle = 'Somos una agencia de viajes que orienta, organiza y acompaña cada proceso con información clara y atención directa.',
  hero_card_badge = 'VIAJES CAROLINA',
  hero_card_title = 'Agencia de viajes',
  hero_note_text = 'Lima, Perú · Atención directa con Carolina',
  accompany_badge = '02 · NUESTRA FORMA DE TRABAJAR',
  accompany_title = 'Tres principios para acompañarte mejor',
  accompany_subtitle = 'Una forma de trabajar clara, cercana y sin complicaciones.',
  accompany_steps_json = '[{"title":"01 · Escuchar primero","body":"Entendemos tus fechas, presupuesto y prioridades."},{"title":"02 · Explicar con claridad","body":"Comparamos opciones y explicamos cada diferencia."},{"title":"03 · Acompañar de verdad","body":"Seguimos disponibles antes, durante y después."}]'::jsonb,
  accompany_quote = 'Una sola asesora. Una conversación continua.',
  advisors_highlights_json = '[{"title":"Orientación personalizada","body":"Basada en tus fechas, presupuesto y prioridades."},{"title":"Seguimiento responsable","body":"Comunicación clara durante todo el proceso."}]'::jsonb
WHERE id = 1;
