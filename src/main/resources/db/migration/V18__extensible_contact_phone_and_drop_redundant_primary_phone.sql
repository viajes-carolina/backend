-- ==============================================================================
-- V18__extensible_contact_phone_and_drop_redundant_primary_phone.sql
-- Viajes Carolina — Normalización: consolida el número de contacto en una sola
-- fuente de verdad (whatsapp_channel), preparada para agregar más números en el
-- futuro (label, is_primary), y elimina site_settings.primary_phone que hoy
-- duplica el mismo dato sin ninguna garantía de que ambos coincidan.
-- ==============================================================================

-- 1. Extender whatsapp_channel para soportar múltiples números a futuro
ALTER TABLE whatsapp_channel ADD COLUMN label VARCHAR(60) NOT NULL DEFAULT 'Línea Principal';
ALTER TABLE whatsapp_channel ADD COLUMN default_message TEXT;
ALTER TABLE whatsapp_channel ADD COLUMN is_primary BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE whatsapp_channel
SET default_message = 'Hola Viajes Carolina, deseo asesoría personalizada para mi próximo viaje.'
WHERE default_message IS NULL;

-- Garantiza que nunca haya más de un número marcado como primario
CREATE UNIQUE INDEX idx_whatsapp_channel_single_primary ON whatsapp_channel (is_primary) WHERE is_primary = TRUE;

-- 2. El id era un singleton fijo (DEFAULT 1); se habilita una secuencia real para
-- poder insertar filas adicionales (otros números) en el futuro sin colisionar.
CREATE SEQUENCE IF NOT EXISTS whatsapp_channel_id_seq OWNED BY whatsapp_channel.id;
SELECT setval('whatsapp_channel_id_seq', (SELECT COALESCE(MAX(id), 1) FROM whatsapp_channel));
ALTER TABLE whatsapp_channel ALTER COLUMN id SET DEFAULT nextval('whatsapp_channel_id_seq');

-- 3. site_settings.primary_phone queda redundante: el número real de contacto
-- (llamadas y WhatsApp son el mismo número en este negocio) vive únicamente en
-- whatsapp_channel a partir de ahora.
ALTER TABLE site_settings DROP COLUMN primary_phone;
