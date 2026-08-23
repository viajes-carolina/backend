package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeTestimonialsSectionDTO;
import com.viajescarolina.api.home.domain.HomeTestimonialsSection;
import com.viajescarolina.api.home.domain.HomeTestimonialsSectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetPublicHomeTestimonialsSectionUseCase {

    @Inject
    HomeTestimonialsSectionRepository testimonialsSectionRepository;

    public HomeTestimonialsSectionDTO execute() {
        HomeTestimonialsSection config = testimonialsSectionRepository.get().orElseGet(() -> new HomeTestimonialsSection(
                1L,
                "05 · Historias reales",
                "Viajes que hoy se recuerdan así",
                "Cada fotografía guarda una experiencia que comenzó con una conversación."
        ));

        return HomeTestimonialsSectionDTO.fromDomain(config);
    }
}
