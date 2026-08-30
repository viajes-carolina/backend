-- ==============================================================================
-- V60__site_settings_mincetur_registration.sql
-- Viajes Carolina — Agrega el número de registro MINCETUR y la ubicación
-- mostrados en la nueva página pública "Constancia MINCETUR" (bounded context
-- legal, ver V59). Estos datos institucionales viven en site_settings junto a
-- legal_company_name/tax_id/mincetur_certificate_url, no en legal_mincetur.
-- ==============================================================================

ALTER TABLE site_settings
  ADD COLUMN mincetur_registration_number VARCHAR(100),
  ADD COLUMN mincetur_location VARCHAR(150) DEFAULT 'Lima, Perú';

UPDATE site_settings
SET mincetur_location = 'Lima, Perú'
WHERE id = 1 AND mincetur_location IS NULL;
