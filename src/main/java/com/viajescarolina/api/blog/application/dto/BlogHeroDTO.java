package com.viajescarolina.api.blog.application.dto;

import com.viajescarolina.api.blog.domain.BlogHero;

public record BlogHeroDTO(
        Long id,
        String eyebrowText,
        String title,
        String description,
        String editionLabel
) {
    public static BlogHeroDTO fromDomain(BlogHero domain) {
        if (domain == null) return null;
        return new BlogHeroDTO(
                domain.getId(),
                domain.getEyebrowText(),
                domain.getTitle(),
                domain.getDescription(),
                domain.getEditionLabel()
        );
    }
}
