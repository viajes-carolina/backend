package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.trust.application.dto.CreateOrUpdateFaqRequest;
import com.viajescarolina.api.trust.application.dto.FaqItemDTO;
import com.viajescarolina.api.trust.domain.FaqItem;
import com.viajescarolina.api.trust.domain.FaqRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateFaqUseCase {

    private final FaqRepository faqRepository;

    @Inject
    public CreateFaqUseCase(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Transactional
    public FaqItemDTO execute(CreateOrUpdateFaqRequest request) {
        FaqItem item = FaqItem.create(
                request.question(),
                request.answer(),
                request.category(),
                request.displayOrder()
        );

        FaqItem saved = faqRepository.save(item);
        return GetPublicTrustUseCase.mapFaqToDTO(saved);
    }
}
