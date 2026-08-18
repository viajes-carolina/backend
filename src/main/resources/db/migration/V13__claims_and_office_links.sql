-- ============================================================================
-- V13__claims_and_office_links.sql
-- Bounded Context: claims & contact exploration links (Corte 13 E2E)
-- ============================================================================

CREATE SEQUENCE IF NOT EXISTS claim_code_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS claim_record (
    id BIGSERIAL PRIMARY KEY,
    claim_code VARCHAR(32) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    document_type VARCHAR(20) NOT NULL, -- DNI, CE, PASAPORTE, RUC
    document_number VARCHAR(30) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    is_minor BOOLEAN NOT NULL DEFAULT FALSE,
    parent_name VARCHAR(150),
    parent_document VARCHAR(30),
    contracted_type VARCHAR(20) NOT NULL DEFAULT 'SERVICIO', -- PRODUCTO, SERVICIO
    claimed_amount NUMERIC(12,2),
    currency VARCHAR(10) NOT NULL DEFAULT 'PEN',
    description VARCHAR(2000) NOT NULL,
    claim_type VARCHAR(20) NOT NULL DEFAULT 'RECLAMO', -- QUEJA, RECLAMO
    consumer_detail VARCHAR(3000) NOT NULL,
    consumer_request VARCHAR(2000) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, IN_PROGRESS, RESOLVED, REJECTED
    response_notes VARCHAR(3000),
    response_at TIMESTAMPTZ,
    turnstile_token VARCHAR(255),
    client_ip_hash VARCHAR(64),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_claim_record_status ON claim_record (status);
CREATE INDEX IF NOT EXISTS idx_claim_record_code ON claim_record (claim_code);
CREATE INDEX IF NOT EXISTS idx_claim_record_created_at ON claim_record (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_claim_record_email ON claim_record (email);

CREATE TABLE IF NOT EXISTS contact_explore_link (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    icon_name VARCHAR(50) NOT NULL,
    target_url VARCHAR(255) NOT NULL,
    button_text VARCHAR(60) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_contact_explore_link_order ON contact_explore_link (display_order, active);

-- Initial Seeds for Contact Explore Links
INSERT INTO contact_explore_link (title, description, icon_name, target_url, button_text, display_order, active)
VALUES
    ('Libro de Reclamaciones', 'Conforme a lo establecido en el Código de Protección y Defensa del Consumidor del Perú.', 'BookOpenIcon', '/reclamaciones', 'Registrar Hoja de Reclamación', 1, TRUE),
    ('Preguntas Frecuentes', 'Resuelve tus dudas sobre reservas, métodos de pago, equipaje y políticas de viaje.', 'QuestionMarkCircleIcon', '/#faq', 'Ver Preguntas Frecuentes', 2, TRUE),
    ('Visita Nuestra Oficina', 'Te atendemos en Miraflores con previa cita para planificar tu itinerario personalizado.', 'MapPinIcon', 'https://maps.google.com/?q=Av.+Larco+101,+Miraflores,+Lima', 'Cómo Llegar en Google Maps', 3, TRUE)
ON CONFLICT DO NOTHING;

-- Initial Seed Demo Claim Record
INSERT INTO claim_record (
    claim_code, full_name, document_type, document_number, email, phone, address,
    is_minor, contracted_type, claimed_amount, currency, description, claim_type,
    consumer_detail, consumer_request, status
)
VALUES (
    'REC-2026-0001',
    'Juan Pérez Alarcón',
    'DNI',
    '45892314',
    'juan.perez@ejemplo.com',
    '+51987112233',
    'Calle Las Flores 230, San Isidro, Lima',
    FALSE,
    'SERVICIO',
    1250.00,
    'PEN',
    'Servicio de paquete turístico a Cusco contratado para Julio 2026',
    'RECLAMO',
    'Deseo solicitar la reprogramación de fechas debido a motivos de fuerza mayor justificados con anticipación.',
    'Reubicación de fecha de viaje sin cobro de penalidad administrativa.',
    'PENDING'
)
ON CONFLICT (claim_code) DO NOTHING;
