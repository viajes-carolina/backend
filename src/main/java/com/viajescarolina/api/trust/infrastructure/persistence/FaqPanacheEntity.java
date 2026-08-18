package com.viajescarolina.api.trust.infrastructure.persistence;

import com.viajescarolina.api.trust.domain.FaqItem;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "faq_item")
public class FaqPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "question", nullable = false, length = 300)
    public String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    public String answer;

    @Column(name = "category", nullable = false, length = 100)
    public String category = "General";

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder = 0;

    @Column(name = "active", nullable = false)
    public boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public FaqItem toDomain() {
        return new FaqItem(
                id,
                question,
                answer,
                category,
                displayOrder,
                active,
                createdAt,
                updatedAt
        );
    }

    public static FaqPanacheEntity fromDomain(FaqItem domain) {
        FaqPanacheEntity entity = new FaqPanacheEntity();
        entity.id = domain.getId();
        entity.question = domain.getQuestion();
        entity.answer = domain.getAnswer();
        entity.category = domain.getCategory();
        entity.displayOrder = domain.getDisplayOrder();
        entity.active = domain.isActive();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }
}
