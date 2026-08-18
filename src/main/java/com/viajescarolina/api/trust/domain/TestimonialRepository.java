package com.viajescarolina.api.trust.domain;

import java.util.List;
import java.util.Optional;

public interface TestimonialRepository {

    List<Testimonial> findAllActive();

    List<Testimonial> findAllTestimonials();

    Optional<Testimonial> findTestimonialById(Long id);

    Testimonial save(Testimonial testimonial);

    void delete(Long id);
}
