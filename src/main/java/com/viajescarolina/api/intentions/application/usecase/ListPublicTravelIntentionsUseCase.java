package com.viajescarolina.api.intentions.application.usecase;

import com.viajescarolina.api.intentions.application.dto.TravelIntentionDTO;
import com.viajescarolina.api.intentions.domain.TravelIntention;
import com.viajescarolina.api.intentions.domain.TravelIntentionRepository;
import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ListPublicTravelIntentionsUseCase {

    private final TravelIntentionRepository intentionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListPublicTravelIntentionsUseCase(
            TravelIntentionRepository intentionRepository,
            MediaRepository mediaRepository) {
        this.intentionRepository = intentionRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<TravelIntentionDTO> execute() {
        return intentionRepository.findAllActive().stream()
                .map(i -> mapToDTO(i, mediaRepository))
                .toList();
    }

    public static TravelIntentionDTO mapToDTO(TravelIntention intention, MediaRepository mediaRepository) {
        String coverUrl = null;
        Double focalX = 50.0;
        Double focalY = 50.0;

        if (intention.getCoverMediaId() != null) {
            Optional<MediaAsset> asset = mediaRepository.findMediaById(intention.getCoverMediaId());
            if (asset.isPresent()) {
                coverUrl = asset.get().getStoragePath();
                if (asset.get().getFocalX() != null) {
                    focalX = asset.get().getFocalX().doubleValue();
                }
                if (asset.get().getFocalY() != null) {
                    focalY = asset.get().getFocalY().doubleValue();
                }
            }
        }

        return new TravelIntentionDTO(
                intention.getId(),
                intention.getSlug(),
                intention.getTitle(),
                intention.getTagline(),
                intention.getIconName(),
                intention.getFeaturedDestinations(),
                intention.getWhatsappMessageTemplate(),
                intention.getCoverMediaId(),
                coverUrl,
                focalX,
                focalY,
                intention.getDisplayOrder(),
                intention.isActive(),
                intention.getCreatedAt(),
                intention.getUpdatedAt()
        );
    }
}
