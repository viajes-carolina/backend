-- ==============================================================================
-- Viajes Carolina — Migration V41: Rediseño de "Contacto" (Figma)
-- ==============================================================================
-- El nuevo diseño elimina el formulario de contacto público (queda intacto en
-- el backend para la bandeja de admin) y reorganiza la página en 3 secciones:
-- Hero conversacional con CTA de WhatsApp, "Cómo empezar" con 3 frases de
-- ejemplo sobre una ruta ondeada, y Oficina/Google Maps.

-- Quedan muertos tras quitar el formulario y la caja de WhatsApp del público
ALTER TABLE contact_page DROP COLUMN IF EXISTS whatsapp_box_title;
ALTER TABLE contact_page DROP COLUMN IF EXISTS whatsapp_box_subtitle;
ALTER TABLE contact_page DROP COLUMN IF EXISTS form_title;
ALTER TABLE contact_page DROP COLUMN IF EXISTS form_subtitle;

-- Hero: CTA y nota (badge/title/subtitle ya existen, se re-siembran con el copy nuevo)
ALTER TABLE contact_page
  ADD COLUMN hero_cta_text VARCHAR(160),
  ADD COLUMN hero_note_text VARCHAR(255);

-- Sección "Cómo empezar" (3 frases de ejemplo sobre una ruta ondeada horizontal)
ALTER TABLE contact_page
  ADD COLUMN starters_badge VARCHAR(160),
  ADD COLUMN starters_title VARCHAR(255),
  ADD COLUMN starters_subtitle TEXT,
  ADD COLUMN starters_closing VARCHAR(255),
  ADD COLUMN starter_phrases_json JSONB NOT NULL DEFAULT '[]';

-- Sección "Oficina y Google Maps"
ALTER TABLE contact_page
  ADD COLUMN office_section_badge VARCHAR(160),
  ADD COLUMN office_section_title VARCHAR(255),
  ADD COLUMN office_section_subtitle TEXT,
  ADD COLUMN office_map_title VARCHAR(255),
  ADD COLUMN office_map_subtitle TEXT,
  ADD COLUMN office_visit_note TEXT;

-- ==============================================================================
-- Seed: copy real del Figma para el singleton contact_page
-- ==============================================================================
UPDATE contact_page SET
  hero_badge = 'UNA CONVERSACIÓN PUEDE SER EL PRIMER PASO',
  hero_title = '¿Qué te gustaría vivir en tu próximo viaje?',
  hero_subtitle = 'No necesitas tener el destino, las fechas ni el presupuesto resueltos. Cuéntanos qué tienes en mente y empezamos a darle forma contigo.',
  hero_cta_text = 'Cuéntanos tu idea por WhatsApp',
  hero_note_text = 'Una persona te lee y te responde.',
  starters_badge = '02 · Puedes empezar con poco',
  starters_title = 'No hace falta llegar con todo resuelto.',
  starters_subtitle = 'Una conversación puede comenzar con lo que ya sabes —o incluso con lo que todavía no sabes.',
  starters_closing = 'Todo puede empezar con una frase.',
  starter_phrases_json = '[{"quote":"Solo tengo unos días libres.","support":"Empezamos por el tiempo disponible."},{"quote":"Tengo un destino en mente.","support":"Revisamos qué experiencia buscas allí."},{"quote":"Todavía necesito ideas.","support":"Te orientamos a partir de tu ritmo y presupuesto."}]'::jsonb,
  office_section_badge = '03 · Nuestra oficina',
  office_section_title = 'Cuando quieras venir, aquí nos encontramos.',
  office_section_subtitle = 'El mapa muestra la ubicación oficial de Viajes Carolina. Para atenderte con tiempo y calma, coordinamos previamente cada visita.',
  office_map_title = 'Visítanos cuando lo necesites.',
  office_map_subtitle = 'Consulta en el mapa la dirección exacta y las indicaciones para llegar.',
  office_visit_note = 'Escríbenos para confirmar el horario y preparar tu atención.'
WHERE id = 1;
