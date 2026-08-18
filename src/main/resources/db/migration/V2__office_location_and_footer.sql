-- ==============================================================================
-- V2__office_location_and_footer.sql
-- Viajes Carolina — Office Location Singleton and Global Footer Support
-- ==============================================================================

-- 1. Tabla Singleton: Oficina Física Principal
CREATE TABLE IF NOT EXISTS office_location (
    id INT PRIMARY KEY DEFAULT 1,
    address_line VARCHAR(200) NOT NULL DEFAULT 'Av. Larco 101, Oficina 502',
    district VARCHAR(100) NOT NULL DEFAULT 'Miraflores',
    city VARCHAR(100) NOT NULL DEFAULT 'Lima',
    country VARCHAR(100) NOT NULL DEFAULT 'Perú',
    postal_code VARCHAR(20) NULL DEFAULT '15074',
    reference_landmark VARCHAR(200) NULL DEFAULT 'A media cuadra del Parque Kennedy',
    latitude NUMERIC(10, 7) NULL DEFAULT -12.1215430,
    longitude NUMERIC(10, 7) NULL DEFAULT -77.0298760,
    google_maps_url VARCHAR(500) NULL DEFAULT 'https://maps.google.com/?q=Miraflores,Lima,Peru',
    embed_maps_url VARCHAR(500) NULL,
    schedule_weekdays VARCHAR(100) NOT NULL DEFAULT 'Lunes a Viernes: 9:00 AM – 7:00 PM',
    schedule_saturdays VARCHAR(100) NOT NULL DEFAULT 'Sábados: 9:00 AM – 2:00 PM',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_office_location_singleton CHECK (id = 1)
);

-- 2. Semilla de Oficina Física
INSERT INTO office_location (
    id, address_line, district, city, country, postal_code, reference_landmark, 
    latitude, longitude, google_maps_url, schedule_weekdays, schedule_saturdays, is_active
) VALUES (
    1, 'Av. Larco 101, Oficina 502', 'Miraflores', 'Lima', 'Perú', '15074', 'A media cuadra del Parque Kennedy',
    -12.1215430, -77.0298760, 'https://maps.google.com/?q=Miraflores,Lima,Peru',
    'Lunes a Viernes: 9:00 AM – 7:00 PM', 'Sábados: 9:00 AM – 2:00 PM', TRUE
) ON CONFLICT (id) DO NOTHING;
