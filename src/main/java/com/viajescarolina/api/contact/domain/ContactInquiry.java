package com.viajescarolina.api.contact.domain;

import java.time.Instant;

public class ContactInquiry {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String destinationOfInterest;
    private String travelDateApprox;
    private int travelersCount;
    private String message;
    private String preferredContactChannel; // 'WHATSAPP', 'EMAIL', 'PHONE'
    private String status; // 'NEW', 'IN_PROGRESS', 'CONTACTED', 'ARCHIVED'
    private String ipHash;
    private boolean turnstileVerified;
    private Instant createdAt;
    private Instant updatedAt;

    public ContactInquiry() {}

    public ContactInquiry(Long id, String fullName, String email, String phone,
                          String destinationOfInterest, String travelDateApprox,
                          int travelersCount, String message, String preferredContactChannel,
                          String status, String ipHash, boolean turnstileVerified,
                          Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.destinationOfInterest = destinationOfInterest;
        this.travelDateApprox = travelDateApprox;
        this.travelersCount = travelersCount;
        this.message = message;
        this.preferredContactChannel = preferredContactChannel;
        this.status = status;
        this.ipHash = ipHash;
        this.turnstileVerified = turnstileVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDestinationOfInterest() { return destinationOfInterest; }
    public void setDestinationOfInterest(String destinationOfInterest) { this.destinationOfInterest = destinationOfInterest; }

    public String getTravelDateApprox() { return travelDateApprox; }
    public void setTravelDateApprox(String travelDateApprox) { this.travelDateApprox = travelDateApprox; }

    public int getTravelersCount() { return travelersCount; }
    public void setTravelersCount(int travelersCount) { this.travelersCount = travelersCount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPreferredContactChannel() { return preferredContactChannel; }
    public void setPreferredContactChannel(String preferredContactChannel) { this.preferredContactChannel = preferredContactChannel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getIpHash() { return ipHash; }
    public void setIpHash(String ipHash) { this.ipHash = ipHash; }

    public boolean isTurnstileVerified() { return turnstileVerified; }
    public void setTurnstileVerified(boolean turnstileVerified) { this.turnstileVerified = turnstileVerified; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
