package com.viajescarolina.api.search.application.usecase;

import com.viajescarolina.api.search.application.dto.GlobalSearchResponseDto;
import com.viajescarolina.api.search.application.dto.SearchResultDto;
import com.viajescarolina.api.search.domain.GlobalSearchRepository;
import com.viajescarolina.api.search.domain.SearchResultItem;
import com.viajescarolina.api.search.domain.SearchResultType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PerformGlobalSearchUseCase {

    private final GlobalSearchRepository searchRepository;

    private static final List<String> DEFAULT_SUGGESTIONS = List.of(
            "Cartagena",
            "Machu Picchu",
            "Punta Cana",
            "Playa & Relax",
            "Consejos de Viaje",
            "Cusco"
    );

    @Inject
    public PerformGlobalSearchUseCase(GlobalSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public GlobalSearchResponseDto execute(String query, String typeStr, Integer limit) {
        String cleanQuery = query != null ? query.trim() : "";
        SearchResultType type = parseType(typeStr);
        int maxResults = (limit != null && limit > 0 && limit <= 50) ? limit : 20;

        List<SearchResultItem> items = searchRepository.search(cleanQuery, type, maxResults);
        List<SearchResultDto> results = items.stream()
                .map(SearchResultDto::fromDomain)
                .toList();

        return new GlobalSearchResponseDto(
                cleanQuery,
                type.name(),
                results.size(),
                results,
                DEFAULT_SUGGESTIONS
        );
    }

    private SearchResultType parseType(String typeStr) {
        if (typeStr == null || typeStr.isBlank()) {
            return SearchResultType.ALL;
        }
        try {
            return SearchResultType.valueOf(typeStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return SearchResultType.ALL;
        }
    }
}
