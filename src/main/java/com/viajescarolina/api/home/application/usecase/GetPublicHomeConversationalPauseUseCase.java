package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeConversationalPauseDTO;
import com.viajescarolina.api.home.domain.HomeConversationalPause;
import com.viajescarolina.api.home.domain.HomeConversationalPauseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetPublicHomeConversationalPauseUseCase {

    @Inject
    HomeConversationalPauseRepository pauseRepository;

    public HomeConversationalPauseDTO execute() {
        HomeConversationalPause config = pauseRepository.get().orElseGet(() -> new HomeConversationalPause(
                1L,
                "04 · ANTES DE SEGUIR",
                "¿Ya imaginas cómo podría sentirse tu próximo viaje?",
                "No necesitas tener todo decidido. Cuéntanos qué te ilusiona y una asesora te ayuda a darle forma.",
                "Conversarlo por WhatsApp",
                "Hola Viajes Carolina, quiero contarles qué tengo en mente para mi próximo viaje."
        ));

        return HomeConversationalPauseDTO.fromDomain(config);
    }
}
