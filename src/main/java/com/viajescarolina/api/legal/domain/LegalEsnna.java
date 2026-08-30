package com.viajescarolina.api.legal.domain;

/**
 * Página pública "Compromiso contra la ESNNA" (/api/public/v1/legal/esnna).
 * Además de los campos comunes, trae el bloque "Declaración institucional"
 * que se muestra antes de las secciones numeradas.
 */
public class LegalEsnna extends AbstractLegalPage {

    private String declarationEyebrow;
    private String declarationTitle;
    private String declarationBody;

    public String getDeclarationEyebrow() { return declarationEyebrow; }
    public void setDeclarationEyebrow(String declarationEyebrow) { this.declarationEyebrow = declarationEyebrow; }

    public String getDeclarationTitle() { return declarationTitle; }
    public void setDeclarationTitle(String declarationTitle) { this.declarationTitle = declarationTitle; }

    public String getDeclarationBody() { return declarationBody; }
    public void setDeclarationBody(String declarationBody) { this.declarationBody = declarationBody; }
}
