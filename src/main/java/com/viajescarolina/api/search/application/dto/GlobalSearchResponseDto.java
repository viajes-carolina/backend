package com.viajescarolina.api.search.application.dto;

import java.util.List;

public record GlobalSearchResponseDto(
    String query,
    String filterType,
    int total,
    List<SearchResultDto> results,
    List<String> suggestedQueries
) {
}
