package com.viajescarolina.api.claims.application.dto;

import com.viajescarolina.api.claims.domain.ContactExploreLink;
import java.time.OffsetDateTime;

public record ContactExploreLinkDTO(
    Long id,
    String title,
    String description,
    String iconName,
    String targetUrl,
    String buttonText,
    Integer displayOrder,
    Boolean active,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static ContactExploreLinkDTO fromDomain(ContactExploreLink entity) {
        return new ContactExploreLinkDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getIconName(),
            entity.getTargetUrl(),
            entity.getButtonText(),
            entity.getDisplayOrder(),
            entity.getActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
