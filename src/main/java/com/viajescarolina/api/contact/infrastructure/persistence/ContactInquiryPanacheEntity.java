package com.viajescarolina.api.contact.infrastructure.persistence;

import com.viajescarolina.api.contact.domain.ContactInquiry;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "contact_inquiry")
public class ContactInquiryPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "full_name", nullable = false)
    public String fullName;

    @Column(name = "email", nullable = false)
    public String email;

    @Column(name = "phone", length = 50)
    public String phone;

    @Column(name = "destination_of_interest", length = 150)
    public String destinationOfInterest;

    @Column(name = "travel_date_approx", length = 100)
    public String travelDateApprox;

    @Column(name = "travelers_count", nullable = false)
    public int travelersCount = 1;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    public String message;

    @Column(name = "preferred_contact_channel", length = 50, nullable = false)
    public String preferredContactChannel = "WHATSAPP";

    @Column(name = "status", length = 50, nullable = false)
    public String status = "NEW";

    @Column(name = "ip_hash", length = 64)
    public String ipHash;

    @Column(name = "turnstile_verified", nullable = false)
    public boolean turnstileVerified = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public ContactInquiry toDomain() {
        return new ContactInquiry(
            id,
            fullName,
            email,
            phone,
            destinationOfInterest,
            travelDateApprox,
            travelersCount,
            message,
            preferredContactChannel,
            status,
            ipHash,
            turnstileVerified,
            createdAt,
            updatedAt
        );
    }

    public static ContactInquiryPanacheEntity fromDomain(ContactInquiry domain) {
        ContactInquiryPanacheEntity entity = new ContactInquiryPanacheEntity();
        entity.id = domain.getId();
        entity.fullName = domain.getFullName();
        entity.email = domain.getEmail();
        entity.phone = domain.getPhone();
        entity.destinationOfInterest = domain.getDestinationOfInterest();
        entity.travelDateApprox = domain.getTravelDateApprox();
        entity.travelersCount = domain.getTravelersCount();
        entity.message = domain.getMessage();
        entity.preferredContactChannel = domain.getPreferredContactChannel() != null ? domain.getPreferredContactChannel() : "WHATSAPP";
        entity.status = domain.getStatus() != null ? domain.getStatus() : "NEW";
        entity.ipHash = domain.getIpHash();
        entity.turnstileVerified = domain.isTurnstileVerified();
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : Instant.now();
        return entity;
    }
}
