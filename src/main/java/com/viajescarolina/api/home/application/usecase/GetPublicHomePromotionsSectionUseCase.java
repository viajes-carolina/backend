package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomePromotionsSectionDTO;
import com.viajescarolina.api.home.domain.HomePromotionsSection;
import com.viajescarolina.api.home.domain.HomePromotionsSectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetPublicHomePromotionsSectionUseCase {

    @Inject
    HomePromotionsSectionRepository promotionsSectionRepository;

    public HomePromotionsSectionDTO execute() {
        HomePromotionsSection config = promotionsSectionRepository.get().orElseGet(() -> new HomePromotionsSection(
                1L,
                "02 · Viajes para empezar a imaginar",
                "Algunas formas de vivir tu próximo viaje",
                "Experiencias que podemos ajustar a tus tiempos, compañía y presupuesto.",
                "¿Cuál de estos viajes te gustaría vivir?",
                "Cuéntanos cuál te gustó",
                "Hola Viajes Carolina, me gustaría conversar sobre una de sus promociones."
        ));

        return HomePromotionsSectionDTO.fromDomain(config);
    }
}
