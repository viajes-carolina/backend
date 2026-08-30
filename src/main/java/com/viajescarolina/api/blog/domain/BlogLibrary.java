package com.viajescarolina.api.blog.domain;

public class BlogLibrary {
    private Long id;
    private String eyebrowText;
    private String title;
    private String description;

    public BlogLibrary() {
    }

    public BlogLibrary(
            Long id,
            String eyebrowText,
            String title,
            String description
    ) {
        this.id = id;
        this.eyebrowText = eyebrowText;
        this.title = title;
        this.description = description;
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
}
