package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeFaqSectionDTO;
import com.viajescarolina.api.home.domain.HomeFaqSection;
import com.viajescarolina.api.home.domain.HomeFaqSectionRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomeFaqSectionUseCase {

    @Inject
    HomeFaqSectionRepository faqSectionRepository;

    @Audited(action = "UPDATE_HOME_FAQ_SECTION", entityType = "HOME_FAQ_SECTION")
    @Transactional
    public HomeFaqSectionDTO execute(HomeFaqSectionDTO dto) {
        HomeFaqSection entity = faqSectionRepository.get().orElseGet(() -> new HomeFaqSection(
                1L,
                "06 · Antes de continuar",
                "Lo que solemos conversar antes de viajar",
                "Es normal tener dudas sobre fechas, pagos o destinos. Aquí respondemos las más frecuentes."
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());

        HomeFaqSection saved = faqSectionRepository.save(entity);
        return HomeFaqSectionDTO.fromDomain(saved);
    }
}
