package com.viajescarolina.api.home.domain;

import java.util.List;

public class HomeConversationalPause {
    private Long id;
    private String badgeText;
    private String title;
    private String subtitle;
    private String whatsappCtaText;
    private String whatsappMessageTemplate;
    private String financingEyebrowText;
    private Integer financingInstallmentsCount;
    private String financingDisclaimerText;
    private List<String> financingBanks;

    public HomeConversationalPause() {
    }

    public HomeConversationalPause(
            Long id,
            String badgeText,
            String title,
            String subtitle,
            String whatsappCtaText,
            String whatsappMessageTemplate,
            String financingEyebrowText,
            Integer financingInstallmentsCount,
            String financingDisclaimerText,
            List<String> financingBanks
    ) {
        this.id = id;
        this.badgeText = badgeText;
        this.title = title;
        this.subtitle = subtitle;
        this.whatsappCtaText = whatsappCtaText;
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.financingEyebrowText = financingEyebrowText;
        this.financingInstallmentsCount = financingInstallmentsCount;
        this.financingDisclaimerText = financingDisclaimerText;
        this.financingBanks = financingBanks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getWhatsappCtaText() {
        return whatsappCtaText;
    }

    public void setWhatsappCtaText(String whatsappCtaText) {
        this.whatsappCtaText = whatsappCtaText;
    }

    public String getWhatsappMessageTemplate() {
        return whatsappMessageTemplate;
    }

    public void setWhatsappMessageTemplate(String whatsappMessageTemplate) {
        this.whatsappMessageTemplate = whatsappMessageTemplate;
    }

    public String getFinancingEyebrowText() {
        return financingEyebrowText;
    }

    public void setFinancingEyebrowText(String financingEyebrowText) {
        this.financingEyebrowText = financingEyebrowText;
    }

    public Integer getFinancingInstallmentsCount() {
        return financingInstallmentsCount;
    }

    public void setFinancingInstallmentsCount(Integer financingInstallmentsCount) {
        this.financingInstallmentsCount = financingInstallmentsCount;
    }

    public String getFinancingDisclaimerText() {
        return financingDisclaimerText;
    }

    public void setFinancingDisclaimerText(String financingDisclaimerText) {
        this.financingDisclaimerText = financingDisclaimerText;
    }

    public List<String> getFinancingBanks() {
        return financingBanks;
    }

    public void setFinancingBanks(List<String> financingBanks) {
        this.financingBanks = financingBanks;
    }
}
