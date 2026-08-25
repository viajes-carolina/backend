-- Simplifica la página de Contacto contra el nuevo Figma: el Hero reemplaza el
-- mockup de chat por una tarjeta "Información de contacto" (WhatsApp/Correo/
-- Horario/Oficina); la sección "Cómo empezar" desaparece por completo; la
-- sección de Oficina retira el bloque "Ubicación oficial" y el CTA de visita
-- (ambos quedaron ocultos en el diseño), dejando solo horario + nota de "antes
-- de venir". Los datos de correo/horario/dirección de la tarjeta del Hero y del
-- panel de horario se resuelven en vivo desde SiteSettings/OfficeLocation (ya
-- son la fuente real), no se duplican como texto estático.

ALTER TABLE contact_page
  DROP COLUMN IF EXISTS hero_chat_label,
  DROP COLUMN IF EXISTS hero_chat_bubble_1,
  DROP COLUMN IF EXISTS hero_chat_bubble_2,
  DROP COLUMN IF EXISTS hero_chat_bubble_3;

ALTER TABLE contact_page
  ADD COLUMN hero_info_title VARCHAR(255),
  ADD COLUMN hero_info_whatsapp_label VARCHAR(160),
  ADD COLUMN hero_info_whatsapp_value VARCHAR(255),
  ADD COLUMN hero_info_email_label VARCHAR(160),
  ADD COLUMN hero_info_schedule_label VARCHAR(160),
  ADD COLUMN hero_info_office_label VARCHAR(160);

ALTER TABLE contact_page
  DROP COLUMN IF EXISTS starters_badge,
  DROP COLUMN IF EXISTS starters_title,
  DROP COLUMN IF EXISTS starters_subtitle,
  DROP COLUMN IF EXISTS starters_closing,
  DROP COLUMN IF EXISTS starter_phrases_json;

ALTER TABLE contact_page
  DROP COLUMN IF EXISTS office_section_subtitle,
  DROP COLUMN IF EXISTS office_map_subtitle,
  DROP COLUMN IF EXISTS office_location_label,
  DROP COLUMN IF EXISTS office_visit_cta_text,
  DROP COLUMN IF EXISTS office_visit_cta_message;

UPDATE contact_page SET
  hero_badge = 'CONTACTO DIRECTO',
  hero_title = '¿Cómo prefieres que conversemos?',
  hero_subtitle = 'La forma más rápida es WhatsApp. También puedes escribirnos o visitarnos en nuestra oficina.',
  hero_cta_text = 'Escríbenos por WhatsApp',
  hero_note_text = 'Te responde una persona, no un formulario.',
  hero_info_title = 'Información de contacto',
  hero_info_whatsapp_label = 'WHATSAPP',
  hero_info_whatsapp_value = 'Atención inmediata',
  hero_info_email_label = 'CORREO',
  hero_info_schedule_label = 'HORARIO',
  hero_info_office_label = 'OFICINA',
  office_section_badge = 'UBICACIÓN',
  office_section_title = 'Encuéntranos en Google Maps.',
  office_map_eyebrow = 'MAPA REAL INTEGRADO',
  office_map_title = 'Horario de atención',
  office_visit_label = 'ANTES DE VENIR',
  office_visit_note = 'Si deseas atención presencial, escríbenos primero por WhatsApp.'
WHERE id = 1;
