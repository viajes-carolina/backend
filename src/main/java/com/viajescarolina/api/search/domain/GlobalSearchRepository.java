package com.viajescarolina.api.search.domain;

import java.util.List;

public interface GlobalSearchRepository {
    List<SearchResultItem> search(String query, SearchResultType type, int limit);
}
