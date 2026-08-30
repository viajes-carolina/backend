package com.viajescarolina.api.blog.application.dto;

import com.viajescarolina.api.blog.domain.BlogLibrary;

public record BlogLibraryDTO(
        Long id,
        String eyebrowText,
        String title,
        String description
) {
    public static BlogLibraryDTO fromDomain(BlogLibrary domain) {
        if (domain == null) return null;
        return new BlogLibraryDTO(
                domain.getId(),
                domain.getEyebrowText(),
                domain.getTitle(),
                domain.getDescription()
        );
    }
}
