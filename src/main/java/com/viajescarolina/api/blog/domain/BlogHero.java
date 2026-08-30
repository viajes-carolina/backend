package com.viajescarolina.api.blog.domain;

public class BlogHero {
    private Long id;
    private String eyebrowText;
    private String title;
    private String description;
    private String editionLabel;

    public BlogHero() {
    }

    public BlogHero(
            Long id,
            String eyebrowText,
            String title,
            String description,
            String editionLabel
    ) {
        this.id = id;
        this.eyebrowText = eyebrowText;
        this.title = title;
        this.description = description;
        this.editionLabel = editionLabel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEyebrowText() {
        return eyebrowText;
    }

    public void setEyebrowText(String eyebrowText) {
        this.eyebrowText = eyebrowText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEditionLabel() {
        return editionLabel;
    }

    public void setEditionLabel(String editionLabel) {
        this.editionLabel = editionLabel;
    }
}
