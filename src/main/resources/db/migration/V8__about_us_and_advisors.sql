-- ==============================================================================
-- Viajes Carolina — Migration V8: About Page (Story, Mission, Vision, Stats) & Travel Advisors
-- ==============================================================================

-- 1. Singleton Table: about_page (Historia, Misión, Visión, Estadísticas y Valores)
CREATE TABLE IF NOT EXISTS about_page (
    id INT PRIMARY KEY DEFAULT 1 CHECK (id = 1),
    hero_badge VARCHAR(100) NOT NULL DEFAULT 'Conoce Nuestra Esencia',
    hero_title VARCHAR(255) NOT NULL DEFAULT 'Más de 12 años transformando viajes en recuerdos inolvidables',
    hero_subtitle TEXT NOT NULL DEFAULT 'Somos una agencia peruana apasionada por diseñar experiencias turísticas personalizadas, con atención cálida y respaldo antes, durante y después de tu viaje.',
    hero_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    story_title VARCHAR(255) NOT NULL DEFAULT 'Nuestra Historia',
    story_body TEXT NOT NULL DEFAULT 'Viajes Carolina nació con el propósito de devolver la calidez humana y la tranquilidad a la planificación de viajes. Lo que comenzó como un sueño familiar hoy es una agencia consolidada en Miraflores, Lima, con un equipo de asesoras especializadas que cuidan cada detalle de tu itinerario.',
    story_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    mission_title VARCHAR(255) NOT NULL DEFAULT 'Nuestra Misión',
    mission_body TEXT NOT NULL DEFAULT 'Brindar asesoría turística experta, honesta y personalizada, garantizando experiencias de viaje seguras y memorables que superen las expectativas de cada familia y viajero.',
    vision_title VARCHAR(255) NOT NULL DEFAULT 'Nuestra Visión',
    vision_body TEXT NOT NULL DEFAULT 'Ser la agencia de viajes boutique referente en el Perú por nuestra excelencia en el servicio al cliente, innovación y acompañamiento constante.',
    values_json JSONB NOT NULL DEFAULT '["Atención humana y cálida", "Transparencia sin letra chica", "Acompañamiento 24/7", "Pasión por los detalles"]'::jsonb,
    experience_years INT NOT NULL DEFAULT 12,
    happy_travelers INT NOT NULL DEFAULT 15000,
    destinations_count INT NOT NULL DEFAULT 85,
    satisfaction_rate_percent INT NOT NULL DEFAULT 99,
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 2. Table: travel_advisor (Equipo de Asesoras Expertas de Viajes Carolina)
CREATE TABLE IF NOT EXISTS travel_advisor (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    role_title VARCHAR(150) NOT NULL,
    specialty VARCHAR(200) NOT NULL,
    bio TEXT NOT NULL,
    photo_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    whatsapp_phone VARCHAR(50),
    whatsapp_message_template TEXT,
    display_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_advisor_active_order ON travel_advisor (active, display_order);

-- 3. Initial Seeds
INSERT INTO about_page (
    id, hero_badge, hero_title, hero_subtitle, hero_media_id,
    story_title, story_body, story_media_id,
    mission_title, mission_body, vision_title, vision_body,
    values_json, experience_years, happy_travelers, destinations_count, satisfaction_rate_percent, revision
) VALUES (
    1,
    'Conoce Nuestra Esencia',
    'Más de 12 años transformando viajes en recuerdos inolvidables',
    'Somos una agencia peruana apasionada por diseñar experiencias turísticas personalizadas, con atención cálida y respaldo antes, durante y después de tu viaje.',
    1,
    'Nuestra Historia',
    'Viajes Carolina nació con el propósito de devolver la calidez humana y la tranquilidad a la planificación de viajes. Lo que comenzó como un sueño familiar hoy es una agencia boutique consolidada en Miraflores, Lima, con un equipo de asesoras especializadas que cuidan cada detalle de tu itinerario.',
    3,
    'Nuestra Misión',
    'Brindar asesoría turística experta, honesta y personalizada, garantizando experiencias de viaje seguras y memorables que superen las expectativas de cada familia y viajero.',
    'Nuestra Visión',
    'Ser la agencia de viajes boutique referente en el Perú por nuestra excelencia en el servicio al cliente, innovación y acompañamiento constante.',
    '["Atención humana y cálida", "Transparencia sin letra chica", "Acompañamiento 24/7", "Pasión por los detalles"]'::jsonb,
    12,
    15000,
    85,
    99,
    1
) ON CONFLICT (id) DO NOTHING;

INSERT INTO travel_advisor (
    id, full_name, role_title, specialty, bio, photo_media_id, whatsapp_phone, whatsapp_message_template, display_order, active
) VALUES
(
    1,
    'Carolina Zúñiga',
    'Fundadora & Directora de Experiencias',
    'Destinos Internacionales & Lunas de Miel',
    'Más de 15 años diseñando itinerarios de ensueño en el Caribe y Europa. Apasionada por hacer de cada viaje una experiencia irrepetible.',
    2,
    '+51987654321',
    'Hola Carolina, me gustaría una asesoría personalizada contigo para planificar mi viaje.',
    1,
    true
),
(
    2,
    'Lucía Ramos',
    'Asesora Senior de Viajes',
    'Caribe, Resorts All-Inclusive & Cruceros',
    'Especialista en vacaciones familiares y escapadas de relax. Conoce al detalle los mejores resorts de Punta Cana, Cancún y Cartagena.',
    2,
    '+51987654321',
    'Hola Lucía, deseo cotizar un paquete al Caribe y recibir tu recomendación de resorts.',
    2,
    true
),
(
    3,
    'Valeria Gómez',
    'Especialista en Destinos Nacionales',
    'Cusco, Selva Amazónica & Trekking',
    'Guía experta en las maravillas del Perú. Diseña rutas culturales y de aventura con los mejores horarios y operadores certificados.',
    3,
    '+51987654321',
    'Hola Valeria, me interesa cotizar un viaje a Cusco / Selva peruana con tu asesoría.',
    3,
    true
)
ON CONFLICT (id) DO NOTHING;
