package com.viajescarolina.api.claims.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ClaimRecord {
    private Long id;
    private String claimCode;
    private String fullName;
    private String documentType;
    private String documentNumber;
    private String email;
    private String phone;
    private String address;
    private boolean isMinor;
    private String parentName;
    private String parentDocument;
    private String contractedType;
    private BigDecimal claimedAmount;
    private String currency;
    private String description;
    private String claimType;
    private String consumerDetail;
    private String consumerRequest;
    private String status;
    private String responseNotes;
    private OffsetDateTime responseAt;
    private String turnstileToken;
    private String clientIpHash;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public ClaimRecord() {}

    public ClaimRecord(Long id, String claimCode, String fullName, String documentType, String documentNumber,
                       String email, String phone, String address, boolean isMinor, String parentName,
                       String parentDocument, String contractedType, BigDecimal claimedAmount, String currency,
                       String description, String claimType, String consumerDetail, String consumerRequest,
                       String status, String responseNotes, OffsetDateTime responseAt, String turnstileToken,
                       String clientIpHash, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.claimCode = claimCode;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isMinor = isMinor;
        this.parentName = parentName;
        this.parentDocument = parentDocument;
        this.contractedType = contractedType;
        this.claimedAmount = claimedAmount;
        this.currency = currency;
        this.description = description;
        this.claimType = claimType;
        this.consumerDetail = consumerDetail;
        this.consumerRequest = consumerRequest;
        this.status = status;
        this.responseNotes = responseNotes;
        this.responseAt = responseAt;
        this.turnstileToken = turnstileToken;
        this.clientIpHash = clientIpHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClaimCode() { return claimCode; }
    public void setClaimCode(String claimCode) { this.claimCode = claimCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isMinor() { return isMinor; }
    public void setMinor(boolean minor) { isMinor = minor; }

    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getParentDocument() { return parentDocument; }
    public void setParentDocument(String parentDocument) { this.parentDocument = parentDocument; }

    public String getContractedType() { return contractedType; }
    public void setContractedType(String contractedType) { this.contractedType = contractedType; }

    public BigDecimal getClaimedAmount() { return claimedAmount; }
    public void setClaimedAmount(BigDecimal claimedAmount) { this.claimedAmount = claimedAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }

    public String getConsumerDetail() { return consumerDetail; }
    public void setConsumerDetail(String consumerDetail) { this.consumerDetail = consumerDetail; }

    public String getConsumerRequest() { return consumerRequest; }
    public void setConsumerRequest(String consumerRequest) { this.consumerRequest = consumerRequest; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResponseNotes() { return responseNotes; }
    public void setResponseNotes(String responseNotes) { this.responseNotes = responseNotes; }

    public OffsetDateTime getResponseAt() { return responseAt; }
    public void setResponseAt(OffsetDateTime responseAt) { this.responseAt = responseAt; }

    public String getTurnstileToken() { return turnstileToken; }
    public void setTurnstileToken(String turnstileToken) { this.turnstileToken = turnstileToken; }

    public String getClientIpHash() { return clientIpHash; }
    public void setClientIpHash(String clientIpHash) { this.clientIpHash = clientIpHash; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
