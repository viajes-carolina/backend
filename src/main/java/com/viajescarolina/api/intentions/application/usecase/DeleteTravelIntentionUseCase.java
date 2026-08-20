package com.viajescarolina.api.intentions.application.usecase;

import com.viajescarolina.api.intentions.domain.TravelIntention;
import com.viajescarolina.api.intentions.domain.TravelIntentionRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteTravelIntentionUseCase {

    private final TravelIntentionRepository intentionRepository;

    @Inject
    public DeleteTravelIntentionUseCase(TravelIntentionRepository intentionRepository) {
        this.intentionRepository = intentionRepository;
    }

    @Audited(action = "DEACTIVATE_TRAVEL_INTENTION", entityType = "TRAVEL_INTENTION")
    @Transactional
    public void execute(Long id) {
        TravelIntention intention = intentionRepository.findIntentionById(id)
                .orElseThrow(() -> new NotFoundException("Intención de viaje no encontrada con ID: " + id));

        intention.deactivate();
        intentionRepository.save(intention);
    }
}
