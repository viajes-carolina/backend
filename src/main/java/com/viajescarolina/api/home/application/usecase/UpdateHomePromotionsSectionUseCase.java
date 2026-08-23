package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomePromotionsSectionDTO;
import com.viajescarolina.api.home.domain.HomePromotionsSection;
import com.viajescarolina.api.home.domain.HomePromotionsSectionRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomePromotionsSectionUseCase {

    @Inject
    HomePromotionsSectionRepository promotionsSectionRepository;

    @Audited(action = "UPDATE_HOME_PROMOTIONS_SECTION", entityType = "HOME_PROMOTIONS_SECTION")
    @Transactional
    public HomePromotionsSectionDTO execute(HomePromotionsSectionDTO dto) {
        HomePromotionsSection entity = promotionsSectionRepository.get().orElseGet(() -> new HomePromotionsSection(
                1L,
                "02 · Viajes para empezar a imaginar",
                "Algunas formas de vivir tu próximo viaje",
                "Experiencias que podemos ajustar a tus tiempos, compañía y presupuesto.",
                "¿Cuál de estos viajes te gustaría vivir?",
                "Cuéntanos cuál te gustó",
                "Hola Viajes Carolina, me gustaría conversar sobre una de sus promociones."
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());
        if (dto.bottomCtaQuestion() != null) entity.setBottomCtaQuestion(dto.bottomCtaQuestion());
        if (dto.bottomCtaWhatsappText() != null) entity.setBottomCtaWhatsappText(dto.bottomCtaWhatsappText());
        if (dto.bottomCtaWhatsappMessage() != null) entity.setBottomCtaWhatsappMessage(dto.bottomCtaWhatsappMessage());

        HomePromotionsSection saved = promotionsSectionRepository.save(entity);
        return HomePromotionsSectionDTO.fromDomain(saved);
    }
}
