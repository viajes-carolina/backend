package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogHeroDTO;
import com.viajescarolina.api.blog.domain.BlogHero;
import com.viajescarolina.api.blog.domain.BlogHeroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetPublicBlogHeroUseCase {

    @Inject
    BlogHeroRepository heroRepository;

    public BlogHeroDTO execute() {
        BlogHero config = heroRepository.get().orElseGet(() -> new BlogHero(
                1L,
                "BITÁCORA · VIAJES CAROLINA",
                "El diario de Viajes Carolina",
                "Guías claras, ideas y respuestas para preparar el viaje con más confianza y menos ruido.",
                "EDICIÓN 01 · AGOSTO 2026"
        ));

        return BlogHeroDTO.fromDomain(config);
    }
}
