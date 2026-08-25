-- El rediseño de Contacto (V41) dejó varios textos visibles en el sitio
-- público como copy fijo en el componente (mensajes de WhatsApp, las 3
-- burbujas del mockup de chat, las etiquetas del mapa) — el usuario pidió
-- explícitamente que todo el contenido de la página sea editable desde el
-- panel admin. Esta migración agrega esos campos faltantes.

-- Hero: mensaje de WhatsApp del CTA + mockup de chat (etiqueta + 3 burbujas)
ALTER TABLE contact_page
  ADD COLUMN hero_cta_message TEXT,
  ADD COLUMN hero_chat_label VARCHAR(160),
  ADD COLUMN hero_chat_bubble_1 TEXT,
  ADD COLUMN hero_chat_bubble_2 TEXT,
  ADD COLUMN hero_chat_bubble_3 TEXT;

-- Sección Oficina: etiquetas del panel de mapa + CTA de visita
ALTER TABLE contact_page
  ADD COLUMN office_map_eyebrow VARCHAR(160),
  ADD COLUMN office_map_pin_title VARCHAR(160),
  ADD COLUMN office_map_pin_subtitle VARCHAR(255),
  ADD COLUMN office_maps_link_text VARCHAR(160),
  ADD COLUMN office_location_label VARCHAR(160),
  ADD COLUMN office_visit_label VARCHAR(160),
  ADD COLUMN office_visit_cta_text VARCHAR(160),
  ADD COLUMN office_visit_cta_message TEXT;

UPDATE contact_page SET
  hero_cta_message = 'Hola Viajes Carolina, quiero contarles qué tengo en mente para mi próximo viaje.',
  hero_chat_label = 'Respuesta humana',
  hero_chat_bubble_1 = 'Tengo unos días libres, pero todavía no sé a dónde ir.',
  hero_chat_bubble_2 = 'Está bien. Empecemos por cómo quieres sentirte y cuánto tiempo tienes.',
  hero_chat_bubble_3 = 'Con eso ya podemos dar el primer paso.',
  office_map_eyebrow = 'Mapa real integrado',
  office_map_pin_title = 'Viajes Carolina',
  office_map_pin_subtitle = 'Ubicación verificada en Google Maps',
  office_maps_link_text = 'Abrir ubicación en Google Maps',
  office_location_label = 'Ubicación oficial',
  office_visit_label = 'Antes de venir',
  office_visit_cta_text = 'Coordinar visita por WhatsApp',
  office_visit_cta_message = 'Hola Viajes Carolina, quisiera coordinar una visita a la oficina.'
WHERE id = 1;
