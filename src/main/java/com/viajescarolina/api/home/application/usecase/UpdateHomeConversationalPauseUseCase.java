package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeConversationalPauseDTO;
import com.viajescarolina.api.home.domain.HomeConversationalPause;
import com.viajescarolina.api.home.domain.HomeConversationalPauseRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

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
                "Hola Viajes Carolina, quiero contarles qué tengo en mente para mi próximo viaje.",
                "Viaja ahora, paga a tu ritmo",
                12,
                "Válido con tarjetas participantes. Sujeto a condiciones de cada entidad financiera.",
                List.of("BCP", "Interbank", "BBVA", "BanBif", "Scotiabank")
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());
        if (dto.whatsappCtaText() != null) entity.setWhatsappCtaText(dto.whatsappCtaText());
        if (dto.whatsappMessageTemplate() != null) entity.setWhatsappMessageTemplate(dto.whatsappMessageTemplate());
        if (dto.financingEyebrowText() != null) entity.setFinancingEyebrowText(dto.financingEyebrowText());
        // A diferencia de los demás campos de este DTO (texto libre, cualquier
        // valor es "válido"), este es numérico con semántica de negocio real —
        // un 0 o negativo produciría "Hasta 0 cuotas sin intereses" en el home
        // público. Se ignora un valor fuera de rango en vez de persistirlo.
        if (dto.financingInstallmentsCount() != null && dto.financingInstallmentsCount() >= 1) {
            entity.setFinancingInstallmentsCount(dto.financingInstallmentsCount());
        }
        if (dto.financingDisclaimerText() != null) entity.setFinancingDisclaimerText(dto.financingDisclaimerText());
        if (dto.financingBanks() != null) entity.setFinancingBanks(dto.financingBanks());

        HomeConversationalPause saved = pauseRepository.save(entity);
        return HomeConversationalPauseDTO.fromDomain(saved);
    }
}
