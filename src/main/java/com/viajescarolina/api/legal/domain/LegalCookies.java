package com.viajescarolina.api.legal.domain;

import java.util.List;

/**
 * Página pública "Política de cookies" (/api/public/v1/legal/cookies).
 * Además de los campos comunes, trae el panel de preferencias de cookies con
 * sus 3 categorías (Esenciales/Analítica/Preferencias) y las etiquetas de los
 * 2 botones de acción.
 */
public class LegalCookies extends AbstractLegalPage {

    private List<CookieCategory> cookieCategories;
    private String acceptAllLabel;
    private String savePreferencesLabel;

    public List<CookieCategory> getCookieCategories() { return cookieCategories; }
    public void setCookieCategories(List<CookieCategory> cookieCategories) { this.cookieCategories = cookieCategories; }

    public String getAcceptAllLabel() { return acceptAllLabel; }
    public void setAcceptAllLabel(String acceptAllLabel) { this.acceptAllLabel = acceptAllLabel; }

    public String getSavePreferencesLabel() { return savePreferencesLabel; }
    public void setSavePreferencesLabel(String savePreferencesLabel) { this.savePreferencesLabel = savePreferencesLabel; }
}
