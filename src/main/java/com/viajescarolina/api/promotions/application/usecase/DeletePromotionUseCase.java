package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeletePromotionUseCase {

    private final PromotionRepository promotionRepository;

    @Inject
    public DeletePromotionUseCase(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public void execute(Long id) {
        Promotion promotion = promotionRepository.findPromotionById(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));

        promotion.deactivate();
        promotionRepository.save(promotion);
    }
}
