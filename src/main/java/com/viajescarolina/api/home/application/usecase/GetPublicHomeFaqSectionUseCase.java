package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeFaqSectionDTO;
import com.viajescarolina.api.home.domain.HomeFaqSection;
import com.viajescarolina.api.home.domain.HomeFaqSectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetPublicHomeFaqSectionUseCase {

    @Inject
    HomeFaqSectionRepository faqSectionRepository;

    public HomeFaqSectionDTO execute() {
        HomeFaqSection config = faqSectionRepository.get().orElseGet(() -> new HomeFaqSection(
                1L,
                "06 · Antes de continuar",
                "Lo que solemos conversar antes de viajar",
                "Es normal tener dudas sobre fechas, pagos o destinos. Aquí respondemos las más frecuentes."
        ));

        return HomeFaqSectionDTO.fromDomain(config);
    }
}
