package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.trust.domain.FaqItem;
import com.viajescarolina.api.trust.domain.FaqRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteFaqUseCase {

    private final FaqRepository faqRepository;

    @Inject
    public DeleteFaqUseCase(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Transactional
    public void execute(Long id) {
        FaqItem item = faqRepository.findFaqById(id)
                .orElseThrow(() -> new NotFoundException("FAQ no encontrado con ID: " + id));

        item.deactivate();
        faqRepository.save(item);
    }
}
