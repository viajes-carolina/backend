-- ==============================================================================
-- V17__fix_schema_drift_and_missing_fks.sql
-- Viajes Carolina — Correcciones de auditoría de esquema (DB-003, DB-004, DB-005)
-- ==============================================================================

-- DB-003: site_settings.logo_media_id / favicon_media_id no tenían FK a media_asset,
-- a diferencia de todas las demás columnas *_media_id del esquema.
ALTER TABLE site_settings
    ADD CONSTRAINT fk_site_settings_logo_media
    FOREIGN KEY (logo_media_id) REFERENCES media_asset(id) ON DELETE SET NULL;

ALTER TABLE site_settings
    ADD CONSTRAINT fk_site_settings_favicon_media
    FOREIGN KEY (favicon_media_id) REFERENCES media_asset(id) ON DELETE SET NULL;

-- DB-004: audit_log.user_id era INT mientras que admin_user.id es BIGINT y la entidad
-- Java (AuditLogPanacheEntity.userId) ya se mapea como Long — se corrige el drift de tipo.
ALTER TABLE audit_log ALTER COLUMN user_id TYPE BIGINT;

-- DB-005: contact_page.id no tenía DEFAULT 1, a diferencia de los otros 6 singletons
-- del esquema (site_settings, whatsapp_channel, office_location, home_hero, about_page,
-- home_blog_inspiration).
ALTER TABLE contact_page ALTER COLUMN id SET DEFAULT 1;
