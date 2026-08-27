package com.viajescarolina.api.home.infrastructure.persistence;

import com.viajescarolina.api.home.domain.HomeConversationalPause;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "home_conversational_pause")
public class HomeConversationalPausePanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "badge_text", nullable = false)
    public String badgeText;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "subtitle", nullable = false, columnDefinition = "TEXT")
    public String subtitle;

    @Column(name = "whatsapp_cta_text", nullable = false)
    public String whatsappCtaText;

    @Column(name = "whatsapp_message_template", nullable = false)
    public String whatsappMessageTemplate;

    @Column(name = "financing_eyebrow_text", nullable = false)
    public String financingEyebrowText;

    @Column(name = "financing_installments_count", nullable = false)
    public Integer financingInstallmentsCount;

    @Column(name = "financing_disclaimer_text", nullable = false, columnDefinition = "TEXT")
    public String financingDisclaimerText;

    @Column(name = "financing_banks_json", columnDefinition = "JSONB")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    public String financingBanksJson = "[]";

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static HomeConversationalPausePanacheEntity fromDomain(HomeConversationalPause domain, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        HomeConversationalPausePanacheEntity entity = new HomeConversationalPausePanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.badgeText = domain.getBadgeText();
        entity.title = domain.getTitle();
        entity.subtitle = domain.getSubtitle();
        entity.whatsappCtaText = domain.getWhatsappCtaText();
        entity.whatsappMessageTemplate = domain.getWhatsappMessageTemplate();
        entity.financingEyebrowText = domain.getFinancingEyebrowText();
        entity.financingInstallmentsCount = domain.getFinancingInstallmentsCount();
        entity.financingDisclaimerText = domain.getFinancingDisclaimerText();
        try {
            entity.financingBanksJson = objectMapper.writeValueAsString(
                    domain.getFinancingBanks() != null ? domain.getFinancingBanks() : java.util.List.of());
        } catch (Exception e) {
            entity.financingBanksJson = "[]";
        }
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public HomeConversationalPause toDomain(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        java.util.List<String> financingBanks = new java.util.ArrayList<>();
        if (financingBanksJson != null && !financingBanksJson.isBlank()) {
            try {
                financingBanks = objectMapper.readValue(
                        financingBanksJson,
                        new com.fasterxml.jackson.core.type.TypeReference<java.util.List<String>>() {});
            } catch (Exception ignored) {
            }
        }

        return new HomeConversationalPause(
                id,
                badgeText,
                title,
                subtitle,
                whatsappCtaText,
                whatsappMessageTemplate,
                financingEyebrowText,
                financingInstallmentsCount,
                financingDisclaimerText,
                financingBanks
        );
    }
}
