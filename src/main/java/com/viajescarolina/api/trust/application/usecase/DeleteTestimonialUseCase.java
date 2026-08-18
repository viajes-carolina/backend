package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteTestimonialUseCase {

    private final TestimonialRepository testimonialRepository;

    @Inject
    public DeleteTestimonialUseCase(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    @Transactional
    public void execute(Long id) {
        Testimonial testimonial = testimonialRepository.findTestimonialById(id)
                .orElseThrow(() -> new NotFoundException("Testimonio no encontrado con ID: " + id));

        testimonial.deactivate();
        testimonialRepository.save(testimonial);
    }
}
