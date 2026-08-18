-- ==============================================================================
-- Migración V5: Tabla travel_intention y semillas oficiales de Figma (Corte 5)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS travel_intention (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(100) UNIQUE NOT NULL,
    title VARCHAR(150) NOT NULL,
    tagline VARCHAR(255) NOT NULL,
    icon_name VARCHAR(50) NOT NULL DEFAULT 'SunIcon',
    featured_destinations JSONB NOT NULL DEFAULT '[]'::jsonb,
    whatsapp_message_template VARCHAR(500) NOT NULL,
    cover_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_travel_intention_order ON travel_intention(display_order ASC, is_active);

-- Semillas oficiales de Figma: 4 Intenciones de Viaje
INSERT INTO travel_intention (
    slug,
    title,
    tagline,
    icon_name,
    featured_destinations,
    whatsapp_message_template,
    cover_media_id,
    display_order,
    is_active
) VALUES 
(
    'playa-relax',
    'Playa & Relax Caribe',
    'Desconéctate frente al mar turquesa con resorts todo incluido y paseos en catamarán.',
    'SunIcon',
    '["Cartagena", "Cancún", "Punta Cana", "San Andrés", "Varadero"]'::jsonb,
    'Hola Viajes Carolina, me interesa planear unas vacaciones de Playa y Relax en el Caribe. ¿Qué opciones tienen disponibles?',
    2,
    1,
    true
),
(
    'cultura-maravillas',
    'Cultura & Maravillas',
    'Conéctate con la historia milenaria, ciudadelas ancestrales y gastronomía única.',
    'LandmarkIcon',
    '["Cusco & Machu Picchu", "Arequipa & Cañón del Colca", "Puno & Lago Titicaca"]'::jsonb,
    'Hola Viajes Carolina, deseo cotizar un viaje cultural a Cusco y maravillas del Perú. ¿Me podrían asesorar?',
    3,
    2,
    true
),
(
    'luna-de-miel',
    'Lunas de Miel & Romance',
    'Experiencias íntimas y exclusivas diseñadas al detalle para celebrar el amor.',
    'HeartIcon',
    '["Riviera Maya", "Bora Bora", "Aruba", "Paracas Deluxe"]'::jsonb,
    'Hola Viajes Carolina, estamos buscando un paquete especial de Luna de Miel. ¿Qué destinos románticos recomiendan?',
    2,
    3,
    true
),
(
    'aventura-naturaleza',
    'Aventura & Naturaleza',
    'Trekking, selva amazónica, avistamiento de fauna y paisajes sobrecogedores.',
    'CompassIcon',
    '["Iquitos & Amazonas", "Huaraz & Cordillera Blanca", "Tarapoto & Cataratas"]'::jsonb,
    'Hola Viajes Carolina, busco un paquete de aventura y ecoturismo. ¿Cuáles son las mejores alternativas?',
    1,
    4,
    true
)
ON CONFLICT (slug) DO NOTHING;
