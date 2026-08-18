-- ==============================================================================
-- Migración V7: Tablas testimonial y faq_item con semillas oficiales de Figma (Corte 7)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS testimonial (
    id BIGSERIAL PRIMARY KEY,
    client_name VARCHAR(150) NOT NULL,
    client_location VARCHAR(120),
    trip_destination VARCHAR(150) NOT NULL,
    comment TEXT NOT NULL,
    rating INT NOT NULL DEFAULT 5 CHECK(rating >= 1 AND rating <= 5),
    avatar_media_id BIGINT REFERENCES media_asset(id) ON DELETE SET NULL,
    consent_confirmed BOOLEAN NOT NULL DEFAULT true,
    display_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS faq_item (
    id BIGSERIAL PRIMARY KEY,
    question VARCHAR(300) NOT NULL,
    answer TEXT NOT NULL,
    category VARCHAR(100) NOT NULL DEFAULT 'General',
    display_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_testimonial_active ON testimonial(active, display_order ASC);
CREATE INDEX IF NOT EXISTS idx_faq_active ON faq_item(active, display_order ASC);

-- Semillas iniciales oficiales de Figma: Testimonios de Clientes
INSERT INTO testimonial (
    client_name,
    client_location,
    trip_destination,
    comment,
    rating,
    avatar_media_id,
    consent_confirmed,
    display_order,
    active
) VALUES
(
    'Mariana & Gonzalo Torres',
    'Lima, Perú',
    'Luna de Miel en Punta Cana',
    'Desde que escribimos por WhatsApp nos atendieron con muchísima paciencia. Nos recomendaron un resort espectacular y el check-in fue sin complicaciones. ¡Totalmente recomendadas!',
    5,
    2,
    true,
    1,
    true
),
(
    'Carlos Mendoza',
    'Arequipa, Perú',
    'Vacaciones Familiares en Cartagena',
    'Excelente asesoría de inicio a fin. Los vuelos, traslados y el hotel estuvieron perfectamente coordinados. Nos dio mucha seguridad tener asistencia durante el viaje.',
    5,
    2,
    true,
    2,
    true
),
(
    'Familia Quispe Valdivia',
    'Trujillo, Perú',
    'Cusco & Machu Picchu 5D/4N',
    'Viajar con niños pequeños siempre es un reto, pero Viajes Carolina organizó los horarios de tren y entradas a la perfección. La experiencia en Machu Picchu fue inolvidable.',
    5,
    3,
    true,
    3,
    true
);

-- Semillas iniciales oficiales de Figma: Preguntas Frecuentes (FAQ)
INSERT INTO faq_item (
    question,
    answer,
    category,
    display_order,
    active
) VALUES
(
    '¿La asesoría para cotizar mi viaje tiene algún costo?',
    'No, nuestra asesoría personalizada por WhatsApp o presencial en oficina es 100% gratuita y sin compromiso. Te brindamos opciones transparentes ajustadas a tu presupuesto.',
    'Asesoría y Cotización',
    1,
    true
),
(
    '¿Qué facilidades de pago aceptan?',
    'Aceptamos transferencias bancarias directas (BCP, BBVA, Interbank), tarjetas de crédito y débito (Visa, Mastercard, Amex) con opciones de cuotas sin intereses según tu entidad financiera.',
    'Pagos y Métodos',
    2,
    true
),
(
    '¿Los paquetes incluyen boletos aéreos y equipaje?',
    'Sí, todos nuestros paquetes detallan explícitamente el tipo de tarifa aérea, equipaje en cabina o bodega incluido y traslados aeropuerto-hotel para que viajes sin sorpresas.',
    'Paquetes y Servicios',
    3,
    true
),
(
    '¿Tienen oficina física donde pueda recibir asesoría personalizada?',
    'Sí, te esperamos en nuestra oficina en Av. Larco 101, Oficina 502, Miraflores, Lima (a media cuadra del Parque Kennedy). Atendemos de Lunes a Viernes de 9:00 AM a 7:00 PM y Sábados de 9:00 AM a 2:00 PM.',
    'Oficina y Ubicación',
    4,
    true
);
