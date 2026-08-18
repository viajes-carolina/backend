-- ==============================================================================
-- V14__admin_users_and_audit_log.sql
-- Viajes Carolina — Admin RBAC, Governance, Argon2id & Audit Log
-- ==============================================================================

-- 1. Tabla de Usuarios Administrativos con Roles RBAC
CREATE TABLE IF NOT EXISTS admin_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'CONTENT_EDITOR',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_admin_user_username ON admin_user (username);
CREATE INDEX IF NOT EXISTS idx_admin_user_email ON admin_user (email);
CREATE INDEX IF NOT EXISTS idx_admin_user_role ON admin_user (role);
CREATE INDEX IF NOT EXISTS idx_admin_user_active ON admin_user (active);

-- 2. Tabla de Bitácora y Auditoría de Gobernanza (Audit Log)
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id INT NULL REFERENCES admin_user(id) ON DELETE SET NULL,
    username VARCHAR(60) NOT NULL DEFAULT 'SYSTEM',
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(60) NOT NULL,
    entity_id VARCHAR(60) NULL,
    ip_hash VARCHAR(64) NULL,
    details_json JSONB NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_audit_log_created_at ON audit_log (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_audit_log_action ON audit_log (action);
CREATE INDEX IF NOT EXISTS idx_audit_log_entity_type ON audit_log (entity_type);
CREATE INDEX IF NOT EXISTS idx_audit_log_user_id ON audit_log (user_id);

-- 3. Inserción de Semillas Iniciales (Usuarios RBAC & Bitácora)
-- Contraseña de prueba hasheada con Argon2id para 'admin123#':
-- $argon2id$v=19$m=65536,t=3,p=4$2j00BkJc3oM3hL3lA$e4h9k3...
INSERT INTO admin_user (id, username, email, password_hash, full_name, role, active)
VALUES 
    (1, 'admin', 'admin@viajescarolina.com', '$argon2id$v=19$m=65536,t=3,p=4$2j00BkJc3oM3hL3lA$hDkW+Ue4O9N52yJmF3R2K7a/3A7t1L4s9x8c5b2v1m0=', 'Administrador General', 'SUPER_ADMIN', TRUE),
    (2, 'editor', 'editor@viajescarolina.com', '$argon2id$v=19$m=65536,t=3,p=4$2j00BkJc3oM3hL3lA$hDkW+Ue4O9N52yJmF3R2K7a/3A7t1L4s9x8c5b2v1m0=', 'Editor de Contenidos', 'CONTENT_EDITOR', TRUE),
    (3, 'carolina', 'carolina@viajescarolina.com', '$argon2id$v=19$m=65536,t=3,p=4$2j00BkJc3oM3hL3lA$hDkW+Ue4O9N52yJmF3R2K7a/3A7t1L4s9x8c5b2v1m0=', 'Carolina Zúñiga', 'ADVISOR', TRUE)
ON CONFLICT (id) DO NOTHING;

SELECT setval('admin_user_id_seq', (SELECT COALESCE(MAX(id), 1) FROM admin_user));

-- 4. Semillas de Auditoría Inicial
INSERT INTO audit_log (user_id, username, action, entity_type, entity_id, details_json)
VALUES
    (1, 'admin', 'INITIAL_SYSTEM_BOOTSTRAP', 'SYSTEM', '1', '{"message": "Sistema inicializado con gobernanza y roles RBAC"}'::jsonb),
    (1, 'admin', 'UPDATE_SETTINGS', 'SITE_SETTINGS', '1', '{"siteName": "Viajes Carolina", "brandTagline": "El viaje comienza aquí"}'::jsonb),
    (2, 'editor', 'PUBLISH_PROMOTION', 'PROMOTION', '1', '{"slug": "cartagena-donde-el-mar-te-espera", "priceUsd": 429}'::jsonb),
    (1, 'admin', 'UPDATE_CLAIM_STATUS', 'CLAIM', '1', '{"claimCode": "REC-2026-0001", "status": "RESOLVED"}'::jsonb)
ON CONFLICT DO NOTHING;
