package com.viajescarolina.api.intentions.application.usecase;

import com.viajescarolina.api.intentions.application.dto.CreateOrUpdateTravelIntentionRequest;
import com.viajescarolina.api.intentions.application.dto.TravelIntentionDTO;
import com.viajescarolina.api.intentions.domain.TravelIntention;
import com.viajescarolina.api.intentions.domain.TravelIntentionRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class CreateTravelIntentionUseCase {

    private final TravelIntentionRepository intentionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public CreateTravelIntentionUseCase(
            TravelIntentionRepository intentionRepository,
            MediaRepository mediaRepository) {
        this.intentionRepository = intentionRepository;
        this.mediaRepository = mediaRepository;
    }

    @Audited(action = "CREATE_TRAVEL_INTENTION", entityType = "TRAVEL_INTENTION")
    @Transactional
    public TravelIntentionDTO execute(CreateOrUpdateTravelIntentionRequest request) {
        if (intentionRepository.findBySlug(request.slug()).isPresent()) {
            throw new BadRequestException("Ya existe una intención de viaje con el slug: " + request.slug());
        }

        TravelIntention intention = TravelIntention.create(
                request.slug(),
                request.title(),
                request.tagline(),
                request.iconName(),
                request.featuredDestinations(),
                request.whatsappMessageTemplate(),
                request.coverMediaId(),
                request.displayOrder()
        );

        TravelIntention saved = intentionRepository.save(intention);
        return ListPublicTravelIntentionsUseCase.mapToDTO(saved, mediaRepository);
    }
}
