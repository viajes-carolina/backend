-- ==============================================================================
-- V1__init_security_and_settings.sql
-- Viajes Carolina — Initial Security, Singletons, and WhatsApp Single Source of Truth
-- ==============================================================================

-- 1. Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- 2. Tabla Singleton: Configuración e Identidad Global del Sitio
CREATE TABLE IF NOT EXISTS site_settings (
    id INT PRIMARY KEY DEFAULT 1,
    site_name VARCHAR(120) NOT NULL DEFAULT 'Viajes Carolina',
    brand_tagline VARCHAR(200) NOT NULL DEFAULT 'El viaje comienza aquí',
    contact_email VARCHAR(150) NOT NULL DEFAULT 'contacto@viajescarolina.com',
    primary_phone VARCHAR(30) NOT NULL DEFAULT '+51 987 654 321',
    logo_media_id INT NULL,
    favicon_media_id INT NULL,
    facebook_url VARCHAR(255) NULL DEFAULT 'https://facebook.com/viajescarolina',
    instagram_url VARCHAR(255) NULL DEFAULT 'https://instagram.com/viajescarolina',
    tiktok_url VARCHAR(255) NULL DEFAULT 'https://tiktok.com/@viajescarolina',
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_site_settings_singleton CHECK (id = 1)
);

-- 3. Tabla Singleton: Canal WhatsApp Centralizado (Single Source of Truth)
CREATE TABLE IF NOT EXISTS whatsapp_channel (
    id INT PRIMARY KEY DEFAULT 1,
    e164_number VARCHAR(20) NOT NULL DEFAULT '+51987654321',
    display_number VARCHAR(30) NOT NULL DEFAULT '+51 987 654 321',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_whatsapp_channel_singleton CHECK (id = 1)
);

-- 4. Tabla de Plantillas de Acciones Contextuales de WhatsApp
CREATE TABLE IF NOT EXISTS whatsapp_action (
    id SERIAL PRIMARY KEY,
    action_key VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    message_template TEXT NOT NULL,
    description VARCHAR(255) NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Inserción de Semillas Iniciales (Seed Data)
INSERT INTO site_settings (id, site_name, brand_tagline, contact_email, primary_phone, facebook_url, instagram_url, tiktok_url)
VALUES (1, 'Viajes Carolina', 'El viaje comienza aquí', 'contacto@viajescarolina.com', '+51 987 654 321', 'https://facebook.com/viajescarolina', 'https://instagram.com/viajescarolina', 'https://tiktok.com/@viajescarolina')
ON CONFLICT (id) DO NOTHING;

INSERT INTO whatsapp_channel (id, e164_number, display_number, is_active)
VALUES (1, '+51987654321', '+51 987 654 321', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO whatsapp_action (action_key, label, message_template, description)
VALUES 
    ('SITE_HEADER_CTA', 'Header CTA', 'Hola Viajes Carolina, deseo solicitar información sobre un viaje.', 'Botón principal del SiteHeader'),
    ('HOME_HERO', 'Hero Principal', 'Hola Viajes Carolina, quiero empezar a planear mi próximo viaje.', 'Botón principal del Hero en Home'),
    ('INTENT_REST', 'Intención Descansar', 'Hola Viajes Carolina, busco un viaje para descansar (playa / relax).', 'Tarjeta de intención de descanso'),
    ('INTENT_ADVENTURE', 'Intención Aventura', 'Hola Viajes Carolina, busco un viaje de aventura y naturaleza.', 'Tarjeta de intención de aventura'),
    ('INTENT_CULTURE', 'Intención Cultura', 'Hola Viajes Carolina, busco un viaje para descubrir historia y cultura.', 'Tarjeta de intención cultural'),
    ('PROMOTION_QUOTE', 'Cotizar Promoción', 'Hola Viajes Carolina, deseo cotizar la promoción: {title} ({price}).', 'Botón cotizar en tarjeta o detalle de promoción'),
    ('BLOG_ADVICE', 'Asesoría desde Blog', 'Hola Viajes Carolina, leí el artículo sobre {title} y quiero asesoría para viajar allá.', 'CTA al pie de artículo de blog'),
    ('ABOUT_CONTACT', 'Contacto desde Nosotros', 'Hola Viajes Carolina, leí sobre su equipo y deseo planificar mi viaje con ustedes.', 'CTA en página Nosotros'),
    ('CONTACT_QUOTE', 'Contacto Cotizar', 'Hola Viajes Carolina, deseo cotizar un viaje a medida con fechas y presupuesto.', 'Opción 1 en página de Contacto'),
    ('CONTACT_IDEAS', 'Contacto Ideas', 'Hola Viajes Carolina, necesito recomendaciones e ideas para viajar.', 'Opción 2 en página de Contacto'),
    ('CONTACT_VISIT', 'Contacto Visita', 'Hola Viajes Carolina, deseo coordinar una visita presencial a su oficina.', 'Opción 3 en página de Contacto'),
    ('HOME_CLOSING', 'Cierre de Inicio', 'Hola Viajes Carolina, estoy listo para planificar mi viaje soñado.', 'CTA de cierre al pie de la Home')
ON CONFLICT (action_key) DO NOTHING;
