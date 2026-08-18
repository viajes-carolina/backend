-- ==============================================================================
-- Migration V10: Blog Posts, Categories and Trigram Search (Corte 10)
-- Bounded Context: com.viajescarolina.api.blog
-- ==============================================================================

-- Enable PostgreSQL pg_trgm extension for fuzzy trigram text search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 1. Table: blog_category
CREATE TABLE IF NOT EXISTS blog_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(140) NOT NULL UNIQUE,
    description TEXT,
    display_order INTEGER NOT NULL DEFAULT 1,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Table: blog_post
CREATE TABLE IF NOT EXISTS blog_post (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(200) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    summary TEXT NOT NULL,
    content_markdown TEXT NOT NULL,
    category_id BIGINT NOT NULL REFERENCES blog_category(id) ON DELETE RESTRICT,
    cover_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    cover_media_url VARCHAR(500),
    author_name VARCHAR(120) NOT NULL DEFAULT 'Equipo Viajes Carolina',
    reading_time_minutes INTEGER NOT NULL DEFAULT 5,
    tags_json JSONB NOT NULL DEFAULT '[]'::jsonb,
    status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED', -- DRAFT, PUBLISHED, ARCHIVED
    published_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    view_count BIGINT NOT NULL DEFAULT 0,
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. Indexes for High Performance & Search
CREATE INDEX IF NOT EXISTS idx_blog_category_slug ON blog_category(slug);
CREATE INDEX IF NOT EXISTS idx_blog_category_active ON blog_category(active, display_order);

CREATE INDEX IF NOT EXISTS idx_blog_post_slug ON blog_post(slug);
CREATE INDEX IF NOT EXISTS idx_blog_post_category_id ON blog_post(category_id);
CREATE INDEX IF NOT EXISTS idx_blog_post_status_published ON blog_post(status, published_at DESC);
CREATE INDEX IF NOT EXISTS idx_blog_post_featured ON blog_post(is_featured) WHERE is_featured = TRUE;
CREATE INDEX IF NOT EXISTS idx_blog_post_tags_gin ON blog_post USING gin(tags_json);

-- GIN Trigram Indexes for instant fuzzy search across titles, summaries and markdown content
CREATE INDEX IF NOT EXISTS idx_blog_post_title_trgm ON blog_post USING gin(title gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_blog_post_summary_trgm ON blog_post USING gin(summary gin_trgm_ops);

-- 4. Initial Seed Data
INSERT INTO blog_category (id, name, slug, description, display_order, active)
VALUES
    (1, 'Guías de Destinos', 'guias-de-destinos', 'Consejos detallados, qué hacer y cómo planificar tu estancia en los mejores destinos del mundo.', 1, TRUE),
    (2, 'Consejos de Viaje', 'consejos-de-viaje', 'Tips prácticos sobre equipaje, documentación, seguros de viaje y cambio de moneda.', 2, TRUE),
    (3, 'Lunas de Miel & Romance', 'lunas-de-miel', 'Inspiración, hoteles boutique y resorts exclusivos para parejas y recién casados.', 3, TRUE),
    (4, 'Gastronomía & Cultura', 'gastronomia-y-cultura', 'Descubre los sabores ancestrales y las tradiciones culturales de cada rincón turístico.', 4, TRUE)
ON CONFLICT (slug) DO NOTHING;

INSERT INTO blog_post (
    id, slug, title, summary, content_markdown, category_id, cover_media_id, cover_media_url,
    author_name, reading_time_minutes, tags_json, status, published_at, view_count, is_featured, active
)
VALUES
    (
        1,
        'guia-completa-para-viajar-a-cartagena-2026',
        'Guía Completa para viajar a Cartagena de Indias: Qué ver, clima y mejores zonas',
        'Descubre por qué Cartagena es la joya del Caribe: desde sus murallas históricas en la Ciudad Amurallada hasta las aguas cristalinas de las Islas del Rosario.',
        '# Guía Completa para viajar a Cartagena de Indias 🌴

Cartagena de Indias es uno de los destinos más vibrantes y coloridos de Sudamérica. Con su arquitectura colonial, atardeceres mágicos sobre el mar Caribe y gastronomía de primer nivel, es perfecta para escapadas de 4 a 5 días.

---

## 1. La Mejor Época para Viajar
El clima en Cartagena es tropical todo el año, con una temperatura promedio de 28°C a 32°C. 
- **Temporada seca (Diciembre a Abril):** Brisa fresca, cielos despejados y poca lluvia. Ideal para días de playa y paseos en catamarán.
- **Temporada verde (Mayo a Noviembre):** Menor afluencia turística, mejores tarifas hoteleras y chaparrones breves al caer la tarde.

---

## 2. Zonas Imperdibles
- **Ciudad Amurallada & San Diego:** Camina entre balcones floridos, plazas históricas y los mejores restaurantes de mariscos.
- **Barrio Getsemaní:** El corazón bohemio y artístico, famoso por su arte urbano, música en vivo y ambiente nocturno.
- **Islas del Rosario & Barú:** A solo 45 minutos en lancha, encontrarás playas de arena blanca y aguas turquesas ideales para snorkel.

---

## 3. Consejos de una Asesora
> *"En Viajes Carolina recomendamos dedicar al menos un día completo a navegar hacia un resort de playa privado en las Islas del Rosario para disfrutar de la tranquilidad sin vendedores ambulantes."* — **Carolina Zúñiga**, Directora de Experiencias.

¿Quieres cotizar tu paquete a Cartagena con vuelos y hotel boutique incluido? ¡Escríbenos por WhatsApp!',
        1,
        2,
        '/media/demo-cartagena-caribe.webp',
        'Carolina Zúñiga',
        6,
        '["Cartagena", "Caribe", "Colombia", "Playas", "Consejos"]'::jsonb,
        'PUBLISHED',
        CURRENT_TIMESTAMP,
        342,
        TRUE,
        TRUE
    ),
    (
        2,
        'machu-picchu-todo-lo-que-debes-saber-antes-de-subir',
        'Machu Picchu: Todo lo que debes saber sobre circuitos, trenes y entradas',
        'Planifica tu visita a la maravilla del mundo sin contratiempos. Te explicamos los nuevos circuitos del santuario, tipos de trenes y aclimatación en Cusco.',
        '# Machu Picchu: Guía Definitiva y Consejos Prácticos 🏔️

Subir a Machu Picchu es el sueño de millones de viajeros. Sin embargo, debido a las regulaciones de conservación y los nuevos circuitos oficiales del Ministerio de Cultura, una planificación anticipada es esencial.

---

## 1. Los Nuevos Circuitos Oficiales
A partir del 2024, el santuario se divide en circuitos específicos:
- **Circuito Panorámico (Terraza Clásica):** Perfecto para la foto postal emblemática y las vistas superiores.
- **Circuito Clásico (Ciudadela Central):** Recorre el Templo del Sol, la Plaza Principal y las terrazas agrícolas.
- **Circuito de la Realeza:** Acceso directo a la zona baja y fuentes ceremoniales.

---

## 2. Tipos de Trenes desde Ollantaytambo
- **Expedition / The 360°:** Opciones cómodas con ventanas panorámicas superiores para apreciar el Valle Sagrado.
- **Vistadome / Observatory:** Vagones con shows folclóricos en vivo, música andina y aperitivos tradicionales.
- **Hiram Bingham (Belmond):** Lujo supremo con comida gourmet y coche bar abierto.

---

## 3. Claves para Evitar el Mal de Altura (Soroche)
1. Pasa al menos el primer día descansando en el Valle Sagrado (2,870 msnm) antes de pernoctar en Cusco ciudad (3,400 msnm).
2. Bebe suficiente mate de coca o infusión de muña.
3. Come ligero durante las primeras 24 horas.

¿Deseas que coordinemos tus boletos de tren y guiado privado en Machu Picchu? ¡Contáctanos y diseñamos tu itinerario!',
        1,
        3,
        '/media/demo-cusco-machupicchu.webp',
        'Valeria Gómez',
        8,
        '["Cusco", "Machu Picchu", "Perú", "Aventura", "Historia"]'::jsonb,
        'PUBLISHED',
        CURRENT_TIMESTAMP,
        518,
        TRUE,
        TRUE
    ),
    (
        3,
        'consejos-para-tu-primer-resort-all-inclusive-en-punta-cana',
        'Consejos para tu primer resort All-Inclusive en Punta Cana',
        'Maximiza tu experiencia todo incluido: cómo elegir entre resorts familiares o solo adultos, reservas de cenas temáticas y propinas.',
        '# Cómo Disfrutar al Máximo un Resort Todo Incluido en Punta Cana 🍹

Punta Cana es el sinónimo universal de desconexión caribeña. Con kilómetros de costa en Playa Bávaro y Uvero Alto, elegir el hotel correcto marcará la diferencia en tus vacaciones.

---

## 1. Solo Adultos vs. Resort Familiar
- **Family-Friendly:** Cuentan con parques acuáticos, clubes infantiles con monitores certificados y restaurantes con menú para niños.
- **Adults-Only:** Piscinas infinity con ambiente chill-out, cenas gourmet a la luz de las velas y serenidad garantizada.

---

## 2. Trucos para las Cenas a la Carta
La mayoría de los resorts 5 estrellas incluyen cenas temáticas (japonesa teppanyaki, francesa, italiana o cortes de carne).
- **Tip Pro:** Descarga la app del hotel apenas hagas el check-in para reservar tus horarios favoritos de cena y evitar filas.

---

## 3. ¿Qué Ropa Empacar?
- Trajes de baño y ropa ligera de lino/algodón.
- Al menos un atuendo *elegante sport* (pantalón largo para caballeros) para los restaurantes temáticos nocturnos.
- Protector solar biodegradable para proteger los arrecifes coralinos.

¡Cotiza tu paquete All-Inclusive a Punta Cana con vuelos y traslados privados con Viajes Carolina!',
        3,
        2,
        '/media/demo-cartagena-caribe.webp',
        'Lucía Ramos',
        5,
        '["Punta Cana", "All Inclusive", "Caribe", "Lunas de Miel", "Resorts"]'::jsonb,
        'PUBLISHED',
        CURRENT_TIMESTAMP,
        289,
        FALSE,
        TRUE
    ),
    (
        4,
        'documentacion-y-seguros-que-necesitas-para-viajar-en-2026',
        'Documentación y seguros: Lo que necesitas saber antes de salir del Perú',
        'Vigencia de pasaporte, requisitos para viajar por Sudamérica con DNI y la importancia de contar con tarjeta de asistencia médica internacional.',
        '# Documentos y Asistencia Médica: Viaja con Total Tranquilidad 📋

Planear un viaje no solo se trata de elegir el destino y el hotel; asegurarte de que tu documentación esté al día te ahorrará sorpresas desagradables en el counter del aeropuerto.

---

## 1. Vigencia del Pasaporte
La mayoría de los países internacionales (incluyendo la Unión Europea, Estados Unidos y países del Caribe) exigen que tu pasaporte tenga **al menos 6 meses de vigencia** a partir de la fecha de retorno.

---

## 2. ¿Cuándo Puedes Viajar Solo con DNI?
Gracias a los acuerdos de la Comunidad Andina y el Mercosur, los ciudadanos peruanos pueden viajar a:
- Colombia, Ecuador, Bolivia, Chile, Argentina, Brasil, Uruguay y Paraguay portando únicamente su **DNI vigente en buen estado**.

---

## 3. La Importancia de la Tarjeta de Asistencia Médica
Un imprevisto de salud en el extranjero puede costar miles de dólares. En Viajes Carolina siempre incluimos planes integrales que cubren:
- Asistencia médica por accidente o enfermedad 24/7.
- Compensación por pérdida o demora de equipaje.
- Telemedicina en español a través de app móvil.

¿Tienes dudas sobre los requisitos de tu próximo destino? ¡Nuestras asesoras te guían paso a paso!',
        2,
        1,
        '/media/demo-hero-travel.webp',
        'Carolina Zúñiga',
        4,
        '["Documentación", "Seguros", "Tips", "Aeropuertos", "DNI"]'::jsonb,
        'PUBLISHED',
        CURRENT_TIMESTAMP,
        195,
        FALSE,
        TRUE
    )
ON CONFLICT (slug) DO NOTHING;

-- Fix Sequence IDs
SELECT setval('blog_category_id_seq', (SELECT COALESCE(MAX(id), 1) FROM blog_category));
SELECT setval('blog_post_id_seq', (SELECT COALESCE(MAX(id), 1) FROM blog_post));
