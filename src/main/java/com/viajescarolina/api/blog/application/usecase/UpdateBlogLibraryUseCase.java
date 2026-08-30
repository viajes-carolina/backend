package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogLibraryDTO;
import com.viajescarolina.api.blog.domain.BlogLibrary;
import com.viajescarolina.api.blog.domain.BlogLibraryRepository;
import com.viajescarolina.api.common.audit.Audited;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateBlogLibraryUseCase {

    @Inject
    BlogLibraryRepository libraryRepository;

    @Audited(action = "UPDATE_BLOG_LIBRARY", entityType = "BLOG_LIBRARY")
    @Transactional
    public BlogLibraryDTO execute(BlogLibraryDTO dto) {
        BlogLibrary entity = libraryRepository.get().orElseGet(() -> new BlogLibrary(
                1L,
                "01 · TODAS LAS HISTORIAS",
                "Explora la bitácora",
                "Busca por tema, filtra por categoría y recorre el archivo a tu ritmo."
        ));

        if (dto.eyebrowText() != null) entity.setEyebrowText(dto.eyebrowText());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.description() != null) entity.setDescription(dto.description());

        BlogLibrary saved = libraryRepository.save(entity);
        return BlogLibraryDTO.fromDomain(saved);
    }
}
