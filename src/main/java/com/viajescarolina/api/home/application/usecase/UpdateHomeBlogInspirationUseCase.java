package com.viajescarolina.api.home.application.usecase;

import com.viajescarolina.api.home.application.dto.HomeBlogInspirationDTO;
import com.viajescarolina.api.home.domain.HomeBlogInspiration;
import com.viajescarolina.api.home.domain.HomeBlogInspirationRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateHomeBlogInspirationUseCase {

    @Inject
    HomeBlogInspirationRepository inspirationRepository;

    @Audited(action = "UPDATE_HOME_BLOG_INSPIRATION", entityType = "HOME_BLOG_INSPIRATION")
    @Transactional
    public HomeBlogInspirationDTO execute(HomeBlogInspirationDTO dto) {
        HomeBlogInspiration entity = inspirationRepository.get().orElseGet(() -> new HomeBlogInspiration(
                1L,
                "Inspiración para tu viaje",
                "Consejos y guías",
                "para explorar el mundo",
                "Descubre recomendaciones de viaje, mejores temporadas, qué empacar y secretos locales de la mano de nuestras asesoras expertas.",
                "Ver todos los artículos del blog",
                "/blog",
                3,
                true
        ));

        if (dto.badgeText() != null) entity.setBadgeText(dto.badgeText());
        if (dto.titleHighlight() != null) entity.setTitleHighlight(dto.titleHighlight());
        if (dto.titleAccent() != null) entity.setTitleAccent(dto.titleAccent());
        if (dto.subtitle() != null) entity.setSubtitle(dto.subtitle());
        if (dto.ctaText() != null) entity.setCtaText(dto.ctaText());
        if (dto.ctaUrl() != null) entity.setCtaUrl(dto.ctaUrl());
        if (dto.postsLimit() != null && dto.postsLimit() > 0) entity.setPostsLimit(dto.postsLimit());
        if (dto.active() != null) entity.setActive(dto.active());

        HomeBlogInspiration saved = inspirationRepository.save(entity);
        return HomeBlogInspirationDTO.fromDomain(saved);
    }
}
