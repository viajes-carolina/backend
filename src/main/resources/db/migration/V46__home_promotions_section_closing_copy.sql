-- Rediseño "más vendible" de la sección de Promociones del Home: el cierre
-- "Propuesta a medida" pasa de un solo texto a eyebrow + título + copy.
-- bottom_cta_question se reutiliza como título del cierre (ya existía).
ALTER TABLE home_promotions_section
  ADD COLUMN bottom_cta_eyebrow VARCHAR(200) NOT NULL DEFAULT 'SI NINGUNO ENCAJA EXACTAMENTE',
  ADD COLUMN bottom_cta_copy TEXT NOT NULL DEFAULT 'Fechas, presupuesto y tipo de viaje: una asesora prepara opciones reales para ti.';
