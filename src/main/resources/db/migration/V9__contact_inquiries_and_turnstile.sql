-- ==============================================================================
-- Flyway Migration V9: Contact Inquiries and Contact Page Singleton (Corte 9)
-- ==============================================================================

-- 1. Tabla singleton de configuración de la página de contacto
CREATE TABLE IF NOT EXISTS contact_page (
    id INTEGER PRIMARY KEY CHECK (id = 1),
    hero_badge VARCHAR(100) NOT NULL DEFAULT 'Estamos para Ayudarte',
    hero_title VARCHAR(255) NOT NULL DEFAULT 'Hablemos de tu próximo destino soñado',
    hero_subtitle TEXT NOT NULL DEFAULT 'Déjanos tus datos o escríbenos directamente por WhatsApp. Una de nuestras asesoras te responderá con una propuesta personalizada.',
    whatsapp_box_title VARCHAR(150) NOT NULL DEFAULT '¿Prefieres atención inmediata?',
    whatsapp_box_subtitle TEXT NOT NULL DEFAULT 'Escríbenos por WhatsApp y una asesora experta te atenderá en minutos en horario de oficina.',
    form_title VARCHAR(150) NOT NULL DEFAULT 'Envíanos un mensaje',
    form_subtitle TEXT NOT NULL DEFAULT 'Completa el formulario y te enviaremos una cotización detallada a tu correo o WhatsApp.',
    revision INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 2. Tabla de solicitudes / leads de contacto
CREATE TABLE IF NOT EXISTS contact_inquiry (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(50),
    destination_of_interest VARCHAR(150),
    travel_date_approx VARCHAR(100),
    travelers_count INTEGER NOT NULL DEFAULT 1,
    message TEXT NOT NULL,
    preferred_contact_channel VARCHAR(50) NOT NULL DEFAULT 'WHATSAPP',
    status VARCHAR(50) NOT NULL DEFAULT 'NEW',
    ip_hash VARCHAR(64),
    turnstile_verified BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Índices estratégicos para búsquedas y filtrado administrativo
CREATE INDEX IF NOT EXISTS idx_contact_inquiry_status ON contact_inquiry (status);
CREATE INDEX IF NOT EXISTS idx_contact_inquiry_created_at ON contact_inquiry (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_contact_inquiry_email ON contact_inquiry (email);

-- Semillas iniciales para contact_page
INSERT INTO contact_page (
    id,
    hero_badge,
    hero_title,
    hero_subtitle,
    whatsapp_box_title,
    whatsapp_box_subtitle,
    form_title,
    form_subtitle,
    revision
) VALUES (
    1,
    'Estamos para Ayudarte',
    'Hablemos de tu próximo destino soñado',
    'Déjanos tus datos o escríbenos directamente por WhatsApp. Una de nuestras asesoras te responderá con una propuesta personalizada.',
    '¿Prefieres atención inmediata?',
    'Escríbenos por WhatsApp y una asesora experta te atenderá en minutos en horario de oficina.',
    'Envíanos un mensaje',
    'Completa el formulario y te enviaremos una cotización detallada a tu correo o WhatsApp.',
    1
) ON CONFLICT (id) DO NOTHING;

-- Semilla inicial de demostración para contact_inquiry
INSERT INTO contact_inquiry (
    id,
    full_name,
    email,
    phone,
    destination_of_interest,
    travel_date_approx,
    travelers_count,
    message,
    preferred_contact_channel,
    status,
    turnstile_verified,
    created_at,
    updated_at
) VALUES (
    1,
    'Andrea Salazar',
    'andrea.salazar@gmail.com',
    '+51998877665',
    'Cartagena de Indias',
    'Noviembre 2026',
    2,
    'Hola, me gustaría cotizar un viaje de 5 días para mi aniversario en Cartagena con hotel frente al mar.',
    'WHATSAPP',
    'NEW',
    TRUE,
    NOW() - INTERVAL '2 hours',
    NOW() - INTERVAL '2 hours'
), (
    2,
    'Jorge Villanueva',
    'jorge.villanueva@outlook.com',
    '+51912345678',
    'Cusco & Machu Picchu',
    'Diciembre 2026',
    4,
    'Deseo cotizar paquete familiar para 2 adultos y 2 niños con tren turístico a Machu Picchu y excursión a Valle Sagrado.',
    'WHATSAPP',
    'IN_PROGRESS',
    TRUE,
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day'
) ON CONFLICT (id) DO NOTHING;

SELECT setval('contact_inquiry_id_seq', (SELECT COALESCE(MAX(id), 1) FROM contact_inquiry));
