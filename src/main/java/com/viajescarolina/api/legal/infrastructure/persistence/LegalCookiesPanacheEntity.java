package com.viajescarolina.api.legal.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.viajescarolina.api.legal.domain.CookieCategory;
import com.viajescarolina.api.legal.domain.LegalCookies;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "legal_cookies")
public class LegalCookiesPanacheEntity extends AbstractLegalPageEntity {

    @Column(name = "cookie_categories_json", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    public String cookieCategoriesJson = "[]";

    @Column(name = "accept_all_label", nullable = false)
    public String acceptAllLabel;

    @Column(name = "save_preferences_label", nullable = false)
    public String savePreferencesLabel;

    public LegalCookies toDomain() {
        LegalCookies domain = copyCommonTo(new LegalCookies());
        domain.setCookieCategories(LegalJsonSupport.readList(cookieCategoriesJson, new TypeReference<List<CookieCategory>>() {}));
        domain.setAcceptAllLabel(acceptAllLabel);
        domain.setSavePreferencesLabel(savePreferencesLabel);
        return domain;
    }

    public static LegalCookiesPanacheEntity fromDomain(LegalCookies domain) {
        LegalCookiesPanacheEntity entity = new LegalCookiesPanacheEntity();
        entity.copyFrom(domain);
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        return entity;
    }

    public void copyFrom(LegalCookies domain) {
        copyCommonFrom(domain);
        this.cookieCategoriesJson = LegalJsonSupport.writeList(domain.getCookieCategories());
        this.acceptAllLabel = domain.getAcceptAllLabel();
        this.savePreferencesLabel = domain.getSavePreferencesLabel();
    }
}
