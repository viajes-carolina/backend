package com.viajescarolina.api.blog.infrastructure.persistence;

import com.viajescarolina.api.blog.domain.BlogLibrary;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "blog_library")
public class BlogLibraryPanacheEntity {

    @Id
    @Column(name = "id")
    public Long id = 1L;

    @Column(name = "eyebrow_text", nullable = false)
    public String eyebrowText;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String description;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public static BlogLibraryPanacheEntity fromDomain(BlogLibrary domain) {
        BlogLibraryPanacheEntity entity = new BlogLibraryPanacheEntity();
        entity.id = domain.getId() != null ? domain.getId() : 1L;
        entity.eyebrowText = domain.getEyebrowText();
        entity.title = domain.getTitle();
        entity.description = domain.getDescription();
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    public BlogLibrary toDomain() {
        return new BlogLibrary(
                id,
                eyebrowText,
                title,
                description
        );
    }
}
