package com.viajescarolina.api.intentions.application.usecase;

import com.viajescarolina.api.intentions.application.dto.CreateOrUpdateTravelIntentionRequest;
import com.viajescarolina.api.intentions.application.dto.TravelIntentionDTO;
import com.viajescarolina.api.intentions.domain.TravelIntention;
import com.viajescarolina.api.intentions.domain.TravelIntentionRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateTravelIntentionUseCase {

    private final TravelIntentionRepository intentionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public UpdateTravelIntentionUseCase(
            TravelIntentionRepository intentionRepository,
            MediaRepository mediaRepository) {
        this.intentionRepository = intentionRepository;
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public TravelIntentionDTO execute(Long id, CreateOrUpdateTravelIntentionRequest request) {
        TravelIntention intention = intentionRepository.findIntentionById(id)
                .orElseThrow(() -> new NotFoundException("Intención de viaje no encontrada con ID: " + id));

        intention.update(
                request.slug(),
                request.title(),
                request.tagline(),
                request.iconName(),
                request.featuredDestinations(),
                request.whatsappMessageTemplate(),
                request.coverMediaId(),
                request.displayOrder() != null ? request.displayOrder() : intention.getDisplayOrder(),
                request.active() != null ? request.active() : intention.isActive()
        );

        TravelIntention saved = intentionRepository.save(intention);
        return ListPublicTravelIntentionsUseCase.mapToDTO(saved, mediaRepository);
    }
}
