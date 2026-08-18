package com.viajescarolina.api.trust.domain;

import java.time.Instant;

public class FaqItem {

    private Long id;
    private String question;
    private String answer;
    private String category;
    private Integer displayOrder;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public FaqItem(
            Long id,
            String question,
            String answer,
            String category,
            Integer displayOrder,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category != null ? category : "General";
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static FaqItem create(
            String question,
            String answer,
            String category,
            Integer displayOrder) {
        return new FaqItem(
                null,
                question,
                answer,
                category,
                displayOrder,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    public void update(
            String question,
            String answer,
            String category,
            Integer displayOrder,
            boolean active) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.displayOrder = displayOrder != null ? displayOrder : this.displayOrder;
        this.active = active;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getCategory() { return category; }
    public Integer getDisplayOrder() { return displayOrder; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
