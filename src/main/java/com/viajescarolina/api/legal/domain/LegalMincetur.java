package com.viajescarolina.api.legal.domain;

/**
 * Página pública "Constancia MINCETUR" (/api/public/v1/legal/mincetur), nueva
 * en el sitio. Además de los campos comunes, trae el bloque "Verificación
 * MINCETUR" con el label del botón externo y la nota de verificación. Los
 * datos concretos del registro (nombre comercial, razón social, N.º de
 * registro, ubicación) NO viven aquí: vienen de site_settings.
 */
public class LegalMincetur extends AbstractLegalPage {

    private String verificationEyebrow;
    private String verificationButtonLabel;
    private String verificationNote;

    public String getVerificationEyebrow() { return verificationEyebrow; }
    public void setVerificationEyebrow(String verificationEyebrow) { this.verificationEyebrow = verificationEyebrow; }

    public String getVerificationButtonLabel() { return verificationButtonLabel; }
    public void setVerificationButtonLabel(String verificationButtonLabel) { this.verificationButtonLabel = verificationButtonLabel; }

    public String getVerificationNote() { return verificationNote; }
    public void setVerificationNote(String verificationNote) { this.verificationNote = verificationNote; }
}
