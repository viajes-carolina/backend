-- ============================================================================
-- V44__claim_extended_fields_and_attachments.sql
-- Bounded Context: claims — asistente público de 4 pasos, adjuntos y constancia PDF
-- ============================================================================

ALTER TABLE claim_record
  ADD COLUMN related_service VARCHAR(30),
  ADD COLUMN reservation_code VARCHAR(60),
  ADD COLUMN service_date DATE,
  ADD COLUMN response_channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL';

CREATE TABLE claim_attachment (
    id BIGSERIAL PRIMARY KEY,
    claim_id BIGINT NOT NULL REFERENCES claim_record(id) ON DELETE CASCADE,
    original_filename VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_claim_attachment_claim_id ON claim_attachment(claim_id);
