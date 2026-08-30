package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogHeroDTO;
import com.viajescarolina.api.blog.domain.BlogHero;
import com.viajescarolina.api.blog.domain.BlogHeroRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateBlogHeroUseCase {

    @Inject
    BlogHeroRepository heroRepository;

    @Audited(action = "UPDATE_BLOG_HERO", entityType = "BLOG_HERO")
    @Transactional
    public BlogHeroDTO execute(BlogHeroDTO dto) {
        BlogHero entity = heroRepository.get().orElseGet(() -> new BlogHero(
                1L,
                "BITÁCORA · VIAJES CAROLINA",
                "El diario de Viajes Carolina",
                "Guías claras, ideas y respuestas para preparar el viaje con más confianza y menos ruido.",
                "EDICIÓN 01 · AGOSTO 2026"
        ));

        if (dto.eyebrowText() != null) entity.setEyebrowText(dto.eyebrowText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.description() != null) entity.setDescription(dto.description());
        if (dto.editionLabel() != null) entity.setEditionLabel(dto.editionLabel());

        BlogHero saved = heroRepository.save(entity);
        return BlogHeroDTO.fromDomain(saved);
    }
}
