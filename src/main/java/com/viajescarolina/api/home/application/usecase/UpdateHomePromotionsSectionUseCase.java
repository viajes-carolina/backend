package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomePromotionsSectionDTO;
import com.viajescarolina.api.home.domain.HomePromotionsSection;
import com.viajescarolina.api.home.domain.HomePromotionsSectionRepository;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomePromotionsSectionUseCase {

    @Inject
    HomePromotionsSectionRepository promotionsSectionRepository;

    @Inject
    MediaRepository mediaRepository;

    @Audited(action = "UPDATE_HOME_PROMOTIONS_SECTION", entityType = "HOME_PROMOTIONS_SECTION")
    @Transactional
    public HomePromotionsSectionDTO execute(HomePromotionsSectionDTO dto) {
        HomePromotionsSection entity = promotionsSectionRepository.get().orElseGet(() -> {
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

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());
        if (dto.bottomCtaQuestion() != null) entity.setBottomCtaQuestion(dto.bottomCtaQuestion());
        if (dto.bottomCtaEyebrow() != null) entity.setBottomCtaEyebrow(dto.bottomCtaEyebrow());
        if (dto.bottomCtaCopy() != null) entity.setBottomCtaCopy(dto.bottomCtaCopy());
        if (dto.bottomCtaWhatsappText() != null) entity.setBottomCtaWhatsappText(dto.bottomCtaWhatsappText());
        if (dto.bottomCtaWhatsappMessage() != null) entity.setBottomCtaWhatsappMessage(dto.bottomCtaWhatsappMessage());

        if (dto.mediaId() != null) {
            Long validMediaId = null;
            if (dto.mediaId() > 0 && mediaRepository.findMediaById(dto.mediaId()).isPresent()) {
                validMediaId = dto.mediaId();
            }
            entity.setMediaId(validMediaId);
        }
        if (dto.mediaUrl() != null && !dto.mediaUrl().isBlank()) entity.setMediaUrl(dto.mediaUrl());
        if (dto.mediaFocalX() != null) entity.setMediaFocalX(dto.mediaFocalX());
        if (dto.mediaFocalY() != null) entity.setMediaFocalY(dto.mediaFocalY());

        HomePromotionsSection saved = promotionsSectionRepository.save(entity);
        return GetPublicHomePromotionsSectionUseCase.mapToDTO(saved, mediaRepository);
    }
}
