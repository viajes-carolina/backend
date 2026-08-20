package com.viajescarolina.api.search.infrastructure.adapter.out.persistence;

import com.viajescarolina.api.search.domain.GlobalSearchRepository;
import com.viajescarolina.api.search.domain.SearchResultItem;
import com.viajescarolina.api.search.domain.SearchResultType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GlobalSearchRepositoryImpl implements GlobalSearchRepository {

    @Inject
    EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<SearchResultItem> search(String query, SearchResultType type, int limit) {
        if (query == null || query.isBlank()) {
            return getInitialSuggestions(type, limit);
        }

        boolean isAll = type == null || type == SearchResultType.ALL;
        String filterSql = isAll ? "" : " AND entity_type = :filterType ";

        // set_limit ajusta el umbral que usa el operador '%' (sargable, sí usa el índice GIN
        // gin_trgm_ops) para igualar el umbral 0.08 que antes solo se aplicaba vía la función
        // similarity(...) en el WHERE, la cual Postgres no puede resolver con el índice (DB-006).
        em.createNativeQuery("SELECT set_limit(0.08)").getSingleResult();

        String sql = """
            SELECT
                entity_type,
                entity_id,
                entity_slug,
                title,
                subtitle,
                metadata_info,
                image_url,
                target_url,
                badge_text,
                (similarity(COALESCE(title, ''), :query) * 2.0 + similarity(COALESCE(subtitle, ''), :query) + similarity(COALESCE(metadata_info, ''), :query)) AS score
            FROM v_global_search_index
            WHERE
                1=1
        """ + filterSql + """
                AND (
                    title ILIKE :likeQuery
                    OR subtitle ILIKE :likeQuery
                    OR metadata_info ILIKE :likeQuery
                    OR title % :query
                    OR subtitle % :query
                )
            ORDER BY score DESC, title ASC
            LIMIT :limit
        """;

        Query q = em.createNativeQuery(sql);
        q.setParameter("query", query);
        q.setParameter("likeQuery", "%" + query + "%");
        if (!isAll) {
            q.setParameter("filterType", type.name());
        }
        q.setParameter("limit", limit);

        List<Object[]> rows = q.getResultList();
        List<SearchResultItem> results = new ArrayList<>(rows.size());

        for (Object[] row : rows) {
            String entityType = (String) row[0];
            Long entityId = ((Number) row[1]).longValue();
            String entitySlug = (String) row[2];
            String title = (String) row[3];
            String subtitle = (String) row[4];
            String metadataInfo = (String) row[5];
            String imageUrl = (String) row[6];
            String targetUrl = (String) row[7];
            String badgeText = (String) row[8];
            Double score = ((Number) row[9]).doubleValue();

            results.add(new SearchResultItem(
                entityType, entityId, entitySlug, title, subtitle,
                metadataInfo, imageUrl, targetUrl, badgeText, score
            ));
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    private List<SearchResultItem> getInitialSuggestions(SearchResultType type, int limit) {
        boolean isAll = type == null || type == SearchResultType.ALL;
        String filterSql = isAll ? "" : " WHERE entity_type = :filterType ";

        String sql = """
            SELECT 
                entity_type,
                entity_id,
                entity_slug,
                title,
                subtitle,
                metadata_info,
                image_url,
                target_url,
                badge_text,
                1.0 AS score
            FROM v_global_search_index
        """ + filterSql + """
            LIMIT :limit
        """;

        Query q = em.createNativeQuery(sql);
        if (!isAll) {
            q.setParameter("filterType", type.name());
        }
        q.setParameter("limit", limit);

        List<Object[]> rows = q.getResultList();
        List<SearchResultItem> results = new ArrayList<>(rows.size());

        for (Object[] row : rows) {
            String entityType = (String) row[0];
            Long entityId = ((Number) row[1]).longValue();
            String entitySlug = (String) row[2];
            String title = (String) row[3];
            String subtitle = (String) row[4];
            String metadataInfo = (String) row[5];
            String imageUrl = (String) row[6];
            String targetUrl = (String) row[7];
            String badgeText = (String) row[8];
            Double score = ((Number) row[9]).doubleValue();

            results.add(new SearchResultItem(
                entityType, entityId, entitySlug, title, subtitle,
                metadataInfo, imageUrl, targetUrl, badgeText, score
            ));
        }

        return results;
    }
}
