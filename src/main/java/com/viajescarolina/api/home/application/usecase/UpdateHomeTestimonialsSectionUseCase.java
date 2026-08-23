package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import com.viajescarolina.api.home.domain.HomeTestimonialsSectionRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomeTestimonialsSectionUseCase {

    @Inject
    HomeTestimonialsSectionRepository testimonialsSectionRepository;

    @Audited(action = "UPDATE_HOME_TESTIMONIALS_SECTION", entityType = "HOME_TESTIMONIALS_SECTION")
    @Transactional
    public HomeTestimonialsSectionDTO execute(HomeTestimonialsSectionDTO dto) {
        HomeTestimonialsSection entity = testimonialsSectionRepository.get().orElseGet(() -> new HomeTestimonialsSection(
                1L,
                "05 · Historias reales",
                "Viajes que hoy se recuerdan así",
                "Cada fotografía guarda una experiencia que comenzó con una conversación."
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());

        HomeTestimonialsSection saved = testimonialsSectionRepository.save(entity);
        return HomeTestimonialsSectionDTO.fromDomain(saved);
    }
}
