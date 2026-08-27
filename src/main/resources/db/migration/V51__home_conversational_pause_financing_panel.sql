-- ==============================================================================
-- V51__home_conversational_pause_financing_panel.sql
-- Viajes Carolina - Rediseño de la sección "04 · Antes de seguir" en Home:
-- agrega el panel de financiamiento ("Hasta N cuotas sin intereses" con bancos
-- participantes) a la tabla singleton home_conversational_pause (V25).
-- ==============================================================================

ALTER TABLE home_conversational_pause
  ADD COLUMN IF NOT EXISTS financing_eyebrow_text VARCHAR(255) NOT NULL DEFAULT 'Viaja ahora, paga a tu ritmo',
  ADD COLUMN IF NOT EXISTS financing_installments_count INTEGER NOT NULL DEFAULT 12,
  ADD COLUMN IF NOT EXISTS financing_disclaimer_text TEXT NOT NULL DEFAULT 'Válido con tarjetas participantes. Sujeto a condiciones de cada entidad financiera.',
  ADD COLUMN IF NOT EXISTS financing_banks_json JSONB NOT NULL DEFAULT '["BCP","Interbank","BBVA","BanBif","Scotiabank"]';
