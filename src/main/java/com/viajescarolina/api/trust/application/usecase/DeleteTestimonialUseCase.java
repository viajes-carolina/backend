package com.viajescarolina.api.trust.application.usecase;

import com.viajescarolina.api.trust.domain.Testimonial;
import com.viajescarolina.api.trust.domain.TestimonialRepository;
import com.viajescarolina.api.common.audit.Audited;
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

    @Audited(action = "DEACTIVATE_TESTIMONIAL", entityType = "TESTIMONIAL")
    @Transactional
    public void execute(Long id) {
        Testimonial testimonial = testimonialRepository.findTestimonialById(id)
                .orElseThrow(() -> new NotFoundException("Testimonio no encontrado con ID: " + id));

        testimonial.deactivate();
        testimonialRepository.save(testimonial);
    }
}
