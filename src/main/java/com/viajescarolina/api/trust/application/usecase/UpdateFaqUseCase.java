package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.trust.application.dto.CreateOrUpdateFaqRequest;
import com.viajescarolina.api.trust.application.dto.FaqItemDTO;
import com.viajescarolina.api.trust.domain.FaqItem;
import com.viajescarolina.api.trust.domain.FaqRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UpdateFaqUseCase {

    private final FaqRepository faqRepository;

    @Inject
    public UpdateFaqUseCase(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Audited(action = "UPDATE_FAQ", entityType = "FAQ")
    @Transactional
    public FaqItemDTO execute(Long id, CreateOrUpdateFaqRequest request) {
        FaqItem item = faqRepository.findFaqById(id)
                .orElseThrow(() -> new NotFoundException("FAQ no encontrado con ID: " + id));

        item.update(
                request.question(),
                request.answer(),
                request.category() != null ? request.category() : item.getCategory(),
                request.displayOrder(),
                request.active() != null ? request.active() : item.isActive()
        );

        FaqItem saved = faqRepository.save(item);
        return GetPublicTrustUseCase.mapFaqToDTO(saved);
    }
}
