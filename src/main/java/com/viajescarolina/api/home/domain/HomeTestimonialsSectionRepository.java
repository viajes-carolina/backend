package com.viajescarolina.api.home.domain;

import java.util.Optional;

public interface HomeTestimonialsSectionRepository {
    Optional<HomeTestimonialsSection> get();
    HomeTestimonialsSection save(HomeTestimonialsSection section);
}
