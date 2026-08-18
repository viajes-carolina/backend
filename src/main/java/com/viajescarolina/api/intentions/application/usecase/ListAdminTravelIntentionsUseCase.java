package com.viajescarolina.api.intentions.application.usecase;

import com.viajescarolina.api.intentions.application.dto.TravelIntentionDTO;
import com.viajescarolina.api.intentions.domain.TravelIntentionRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListAdminTravelIntentionsUseCase {

    private final TravelIntentionRepository intentionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListAdminTravelIntentionsUseCase(
            TravelIntentionRepository intentionRepository,
            MediaRepository mediaRepository) {
        this.intentionRepository = intentionRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<TravelIntentionDTO> execute() {
        return intentionRepository.findAllIntentions().stream()
                .map(i -> ListPublicTravelIntentionsUseCase.mapToDTO(i, mediaRepository))
                .toList();
    }
}
