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
        HomePromotionsSection config = promotionsSectionRepository.get().orElseGet(() -> {
            HomePromotionsSection fallback = new HomePromotionsSection(
                    1L,
                    "02 · Viajes para empezar a imaginar",
                    "Algunas formas de vivir tu próximo viaje",
                    "Experiencias que podemos ajustar a tus tiempos, compañía y presupuesto.",
                    "¿Cuál de estos viajes te gustaría vivir?",
                    "Cuéntanos cuál te gustó",
                    "Hola Viajes Carolina, me gustaría conversar sobre una de sus promociones."
            );
            fallback.setBottomCtaEyebrow("SI NINGUNO ENCAJA EXACTAMENTE");
            fallback.setBottomCtaCopy("Fechas, presupuesto y tipo de viaje: una asesora prepara opciones reales para ti.");
            return fallback;
        });

        return mapToDTO(config);
    }

    public static HomePromotionsSectionDTO mapToDTO(HomePromotionsSection config) {
        return new HomePromotionsSectionDTO(
                config.getId(),
                config.getBadgeText(),
                config.getTitle(),
                config.getSubtitle(),
                config.getBottomCtaQuestion(),
                config.getBottomCtaEyebrow(),
                config.getBottomCtaCopy(),
                config.getBottomCtaWhatsappText(),
                config.getBottomCtaWhatsappMessage()
        );
    }
}
