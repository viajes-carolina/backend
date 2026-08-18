package com.viajescarolina.api.search.application.dto;

import com.viajescarolina.api.search.domain.SearchResultItem;

public record SearchResultDto(
    String entityType,
    Long entityId,
    String entitySlug,
    String title,
    String subtitle,
    String metadataInfo,
    String imageUrl,
    String targetUrl,
    String badgeText,
    Double score
) {
    public static SearchResultDto fromDomain(SearchResultItem item) {
        return new SearchResultDto(
            item.getEntityType(),
            item.getEntityId(),
            item.getEntitySlug(),
            item.getTitle(),
            item.getSubtitle(),
            item.getMetadataInfo(),
            item.getImageUrl(),
            item.getTargetUrl(),
            item.getBadgeText(),
            item.getScore()
        );
    }
}
