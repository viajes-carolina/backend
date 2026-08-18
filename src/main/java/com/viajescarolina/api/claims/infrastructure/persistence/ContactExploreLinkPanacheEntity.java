package com.viajescarolina.api.claims.infrastructure.persistence;

import com.viajescarolina.api.claims.domain.ContactExploreLink;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "contact_explore_link")
public class ContactExploreLinkPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100)
    public String title;

    @Column(nullable = false, length = 255)
    public String description;

    @Column(name = "icon_name", nullable = false, length = 50)
    public String iconName;

    @Column(name = "target_url", nullable = false, length = 255)
    public String targetUrl;

    @Column(name = "button_text", nullable = false, length = 60)
    public String buttonText;

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder;

    @Column(nullable = false)
    public Boolean active;

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public OffsetDateTime updatedAt;

    public ContactExploreLink toDomain() {
        return new ContactExploreLink(
            id, title, description, iconName, targetUrl, buttonText, displayOrder, active, createdAt, updatedAt
        );
    }

    public static ContactExploreLinkPanacheEntity fromDomain(ContactExploreLink domain) {
        ContactExploreLinkPanacheEntity entity = new ContactExploreLinkPanacheEntity();
        entity.id = domain.getId();
        entity.title = domain.getTitle();
        entity.description = domain.getDescription();
        entity.iconName = domain.getIconName();
        entity.targetUrl = domain.getTargetUrl();
        entity.buttonText = domain.getButtonText();
        entity.displayOrder = domain.getDisplayOrder() != null ? domain.getDisplayOrder() : 0;
        entity.active = domain.getActive() != null ? domain.getActive() : true;
        entity.createdAt = domain.getCreatedAt() != null ? domain.getCreatedAt() : OffsetDateTime.now();
        entity.updatedAt = domain.getUpdatedAt() != null ? domain.getUpdatedAt() : OffsetDateTime.now();
        return entity;
    }
}
