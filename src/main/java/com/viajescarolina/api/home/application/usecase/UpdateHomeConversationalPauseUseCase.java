package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeConversationalPauseDTO;
import com.viajescarolina.api.home.domain.HomeConversationalPause;
import com.viajescarolina.api.home.domain.HomeConversationalPauseRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomeConversationalPauseUseCase {

    @Inject
    HomeConversationalPauseRepository pauseRepository;

    @Audited(action = "UPDATE_HOME_CONVERSATIONAL_PAUSE", entityType = "HOME_CONVERSATIONAL_PAUSE")
    @Transactional
    public HomeConversationalPauseDTO execute(HomeConversationalPauseDTO dto) {
        HomeConversationalPause entity = pauseRepository.get().orElseGet(() -> new HomeConversationalPause(
                1L,
                "04 · ANTES DE SEGUIR",
                "¿Ya imaginas cómo podría sentirse tu próximo viaje?",
                "No necesitas tener todo decidido. Cuéntanos qué te ilusiona y una asesora te ayuda a darle forma.",
                "Conversarlo por WhatsApp",
                "Hola Viajes Carolina, quiero contarles qué tengo en mente para mi próximo viaje."
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());
        if (dto.whatsappCtaText() != null) entity.setWhatsappCtaText(dto.whatsappCtaText());
        if (dto.whatsappMessageTemplate() != null) entity.setWhatsappMessageTemplate(dto.whatsappMessageTemplate());

        HomeConversationalPause saved = pauseRepository.save(entity);
        return HomeConversationalPauseDTO.fromDomain(saved);
    }
}
