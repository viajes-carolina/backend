package com.viajescarolina.api.blog.domain;

import java.time.Instant;
import java.util.List;

public class BlogPost {
    private Long id;
    private String slug;
    private String title;
    private String summary;
    private String contentMarkdown;
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private Long coverMediaId;
    private Double coverFocalX;
    private Double coverFocalY;
    private Long authorAdvisorId;
    private Integer readingTimeMinutes;
    private List<String> tags;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private Instant publishedAt;
    private Long viewCount;
    private Boolean isFeatured;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public BlogPost() {
    }

    public BlogPost(
            Long id,
            String slug,
            String title,
            String summary,
            String contentMarkdown,
            Long categoryId,
            String categoryName,
            String categorySlug,
            Long coverMediaId,
            Double coverFocalX,
            Double coverFocalY,
            Long authorAdvisorId,
            Integer readingTimeMinutes,
            List<String> tags,
            String status,
            Instant publishedAt,
            Long viewCount,
            Boolean isFeatured,
            Boolean active,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.summary = summary;
        this.contentMarkdown = contentMarkdown;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.coverMediaId = coverMediaId;
        this.coverFocalX = coverFocalX;
        this.coverFocalY = coverFocalY;
        this.authorAdvisorId = authorAdvisorId;
        this.readingTimeMinutes = readingTimeMinutes;
        this.tags = tags;
        this.status = status;
        this.publishedAt = publishedAt;
        this.viewCount = viewCount;
        this.isFeatured = isFeatured;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentMarkdown() {
        return contentMarkdown;
    }

    public void setContentMarkdown(String contentMarkdown) {
        this.contentMarkdown = contentMarkdown;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public Long getCoverMediaId() {
        return coverMediaId;
    }

    public void setCoverMediaId(Long coverMediaId) {
        this.coverMediaId = coverMediaId;
    }

    public Double getCoverFocalX() {
        return coverFocalX;
    }

    public void setCoverFocalX(Double coverFocalX) {
        this.coverFocalX = coverFocalX;
    }

    public Double getCoverFocalY() {
        return coverFocalY;
    }

    public void setCoverFocalY(Double coverFocalY) {
        this.coverFocalY = coverFocalY;
    }

    public Long getAuthorAdvisorId() {
        return authorAdvisorId;
    }

    public void setAuthorAdvisorId(Long authorAdvisorId) {
        this.authorAdvisorId = authorAdvisorId;
    }

    public Integer getReadingTimeMinutes() {
        return readingTimeMinutes;
    }

    public void setReadingTimeMinutes(Integer readingTimeMinutes) {
        this.readingTimeMinutes = readingTimeMinutes;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
