package com.viajescarolina.api.blog.infrastructure.persistence;

import com.viajescarolina.api.blog.domain.BlogHero;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "blog_hero")
public class BlogHeroPanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "eyebrow_text", nullable = false)
    public String eyebrowText;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String description;

    @Column(name = "edition_label", nullable = false)
    public String editionLabel;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static BlogHeroPanacheEntity fromDomain(BlogHero domain) {
        BlogHeroPanacheEntity entity = new BlogHeroPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.eyebrowText = domain.getEyebrowText();
        entity.title = domain.getTitle();
        entity.description = domain.getDescription();
        entity.editionLabel = domain.getEditionLabel();
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public BlogHero toDomain() {
        return new BlogHero(
                id,
                eyebrowText,
                title,
                description,
                editionLabel
        );
    }
}
