package com.viajescarolina.api.blog.application.usecase;

import com.viajescarolina.api.blog.application.dto.BlogLibraryDTO;
import com.viajescarolina.api.blog.domain.BlogLibrary;
import com.viajescarolina.api.blog.domain.BlogLibraryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetPublicBlogLibraryUseCase {

    @Inject
    BlogLibraryRepository libraryRepository;

    public BlogLibraryDTO execute() {
        BlogLibrary config = libraryRepository.get().orElseGet(() -> new BlogLibrary(
                1L,
                "01 · TODAS LAS HISTORIAS",
                "Explora la bitácora",
                "Busca por tema, filtra por categoría y recorre el archivo a tu ritmo."
        ));

        return BlogLibraryDTO.fromDomain(config);
    }
}
