package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.trust.application.dto.FaqItemDTO;
import com.viajescarolina.api.trust.domain.FaqRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListAdminFaqsUseCase {

    private final FaqRepository faqRepository;

    @Inject
    public ListAdminFaqsUseCase(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FaqItemDTO> execute() {
        return faqRepository.findAllFaqs().stream()
                .map(GetPublicTrustUseCase::mapFaqToDTO)
                .toList();
    }
}
