package com.viajescarolina.api.claims.application.usecase;

import com.viajescarolina.api.claims.application.dto.ContactExploreLinkDTO;
import com.viajescarolina.api.claims.domain.ContactExploreLinkRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListContactExploreLinksUseCase {

    private final ContactExploreLinkRepository exploreLinkRepository;

    @Inject
    public ListContactExploreLinksUseCase(ContactExploreLinkRepository exploreLinkRepository) {
        this.exploreLinkRepository = exploreLinkRepository;
    }

    public List<ContactExploreLinkDTO> execute() {
        return exploreLinkRepository.findActiveOrdered().stream()
                .map(ContactExploreLinkDTO::fromDomain)
                .toList();
    }
}
