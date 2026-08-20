-- ==============================================================================
-- V16__add_legal_info_to_site_settings.sql
-- Viajes Carolina — Agrega razón social y RUC a site_settings (hallazgo de
-- auditoría FE-006 / SEC: estos datos legales, obligatorios en el Libro de
-- Reclamaciones por la Ley N° 29571, estaban hardcodeados en el frontend sin
-- ningún campo editable en el backend).
-- ==============================================================================

ALTER TABLE site_settings ADD COLUMN legal_company_name VARCHAR(200);
ALTER TABLE site_settings ADD COLUMN tax_id VARCHAR(20);

UPDATE site_settings
SET legal_company_name = 'VIAJES CAROLINA S.A.C.', tax_id = '20601234567'
WHERE id = 1 AND legal_company_name IS NULL;
