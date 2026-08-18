package com.viajescarolina.api.blog.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "blog_post")
public class BlogPostPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 200)
    public String slug;

    @Column(nullable = false, length = 255)
    public String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String summary;

    @Column(name = "content_markdown", nullable = false, columnDefinition = "TEXT")
    public String contentMarkdown;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    public BlogCategoryPanacheEntity category;

    @Column(name = "cover_media_id")
    public Long coverMediaId;

    @Column(name = "cover_media_url", length = 500)
    public String coverMediaUrl;

    @Column(name = "author_name", nullable = false, length = 120)
    public String authorName = "Equipo Viajes Carolina";

    @Column(name = "reading_time_minutes", nullable = false)
    public Integer readingTimeMinutes = 5;

    @Column(name = "tags_json", columnDefinition = "JSONB")
    public String tagsJson = "[]";

    @Column(nullable = false, length = 30)
    public String status = "PUBLISHED";

    @Column(name = "published_at")
    public Instant publishedAt = Instant.now();

    @Column(name = "view_count", nullable = false)
    public Long viewCount = 0L;

    @Column(name = "is_featured", nullable = false)
    public Boolean isFeatured = false;

    @Column(nullable = false)
    public Boolean active = true;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt = Instant.now();
}
