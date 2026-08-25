package com.viajescarolina.api.about.domain;

import java.time.Instant;

public class TravelAdvisor {
    private Long id;
    private String fullName;
    private String roleTitle;
    private String specialty;
    private String bio;
    private String quote;
    private Long photoMediaId;
    private String photoMediaUrl;
    private String whatsappPhone;
    private String whatsappMessageTemplate;
    private int displayOrder;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public TravelAdvisor() {}

    public TravelAdvisor(Long id, String fullName, String roleTitle, String specialty, String bio, String quote,
                         Long photoMediaId, String photoMediaUrl, String whatsappPhone,
                         String whatsappMessageTemplate, int displayOrder, boolean active,
                         Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.roleTitle = roleTitle;
        this.specialty = specialty;
        this.bio = bio;
        this.quote = quote;
        this.photoMediaId = photoMediaId;
        this.photoMediaUrl = photoMediaUrl;
        this.whatsappPhone = whatsappPhone;
        this.whatsappMessageTemplate = whatsappMessageTemplate;
        this.displayOrder = displayOrder;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public Long getPhotoMediaId() { return photoMediaId; }
    public void setPhotoMediaId(Long photoMediaId) { this.photoMediaId = photoMediaId; }

    public String getPhotoMediaUrl() { return photoMediaUrl; }
    public void setPhotoMediaUrl(String photoMediaUrl) { this.photoMediaUrl = photoMediaUrl; }

    public String getWhatsappPhone() { return whatsappPhone; }
    public void setWhatsappPhone(String whatsappPhone) { this.whatsappPhone = whatsappPhone; }

    public String getWhatsappMessageTemplate() { return whatsappMessageTemplate; }
    public void setWhatsappMessageTemplate(String whatsappMessageTemplate) { this.whatsappMessageTemplate = whatsappMessageTemplate; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
