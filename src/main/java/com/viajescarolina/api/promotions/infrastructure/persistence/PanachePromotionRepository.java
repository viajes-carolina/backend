package com.viajescarolina.api.promotions.infrastructure.persistence;

import com.viajescarolina.api.promotions.domain.AdminPromotionFilter;
import com.viajescarolina.api.promotions.domain.HomeFeaturedPolicy;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionCatalogCounters;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class PanachePromotionRepository implements PromotionRepository, PanacheRepositoryBase<PromotionPanacheEntity, Long> {

    /**
     * El criterio de portada, escrito UNA vez (ver {@link HomeFeaturedPolicy}): activas,
     * las más recientes primero, con el ID como desempate estable cuando dos comparten
     * {@code created_at} (las sembradas por las migraciones lo hacen).
     */
    private static final String HOME_FEATURED_CRITERIA = "active = true ORDER BY createdAt DESC, id DESC";

    /** Mismo orden que la portada, aplicado a todo el catálogo del panel. */
    private static final String ADMIN_ORDER = " ORDER BY p.created_at DESC, p.id DESC";

    // ------------------------------------------------------------------ portada

    private PanacheQuery<PromotionPanacheEntity> homeFeaturedQuery() {
        return find(HOME_FEATURED_CRITERIA).page(0, HomeFeaturedPolicy.SLOTS);
    }

    @Override
    public List<Promotion> findHomeFeatured() {
        return homeFeaturedQuery().list().stream()
                .map(PromotionPanacheEntity::toDomain)
                .toList();
    }

    @Override
    public List<Long> findHomeFeaturedIds() {
        // Deliberadamente sobre la MISMA consulta que findHomeFeatured(): traer 3 filas de más
        // no se nota, y a cambio es imposible que la marca "en portada" del panel y lo que
        // Inicio muestra de verdad se desincronicen.
        return homeFeaturedQuery().list().stream()
                .map(entity -> entity.id)
                .toList();
    }

    // ------------------------------------------------------------------ listado del panel

    @Override
    public List<Promotion> findAdminPage(AdminPromotionFilter filter, int page, int size) {
        Criteria criteria = buildCriteria(filter);
        // SQL nativo (y no HQL) por el ILIKE: es el operador que los índices GIN pg_trgm de la
        // migración V11 (idx_promotion_title_trgm, _dest_trgm, _summary_trgm) saben acelerar.
        // HQL no lo expresa y su alternativa, lower(title) LIKE ..., no es sargable contra
        // esos índices porque están construidos sobre la columna cruda.
        Query query = getEntityManager().createNativeQuery(
                "SELECT p.* FROM promotion p WHERE " + criteria.where()
                        + ADMIN_ORDER + " LIMIT :limite OFFSET :desplazamiento",
                PromotionPanacheEntity.class);
        criteria.bind(query);
        query.setParameter("limite", size);
        query.setParameter("desplazamiento", (long) page * size);

        @SuppressWarnings("unchecked")
        List<PromotionPanacheEntity> rows = query.getResultList();
        return rows.stream().map(PromotionPanacheEntity::toDomain).toList();
    }

    @Override
    public long countAdminPage(AdminPromotionFilter filter) {
        Criteria criteria = buildCriteria(filter);
        Query query = getEntityManager().createNativeQuery(
                "SELECT count(*) FROM promotion p WHERE " + criteria.where());
        criteria.bind(query);
        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public PromotionCatalogCounters countCatalog() {
        // Los tres contadores en una sola pasada con FILTER, en vez de tres count(*) separados.
        Object[] row = (Object[]) getEntityManager().createNativeQuery("""
                SELECT count(*),
                       count(*) FILTER (WHERE facebook_permalink_url IS NOT NULL),
                       count(*) FILTER (WHERE active = false)
                FROM promotion
                """).getSingleResult();
        return new PromotionCatalogCounters(
                ((Number) row[0]).longValue(),
                ((Number) row[1]).longValue(),
                ((Number) row[2]).longValue());
    }

    /**
     * Traduce los filtros del panel a una cláusula WHERE parametrizada. Todo valor de usuario
     * viaja como parámetro con nombre; lo único que se concatena son fragmentos de SQL fijos
     * elegidos por este método.
     */
    private Criteria buildCriteria(AdminPromotionFilter filter) {
        StringBuilder where = new StringBuilder("1 = 1");
        Map<String, Object> params = new LinkedHashMap<>();

        if (filter.hasSearch()) {
            where.append(" AND (p.title ILIKE :busqueda OR p.destination ILIKE :busqueda OR p.summary ILIKE :busqueda)");
            params.put("busqueda", toLikePattern(filter.search()));
        }

        if (filter.status() != null) {
            switch (filter.status()) {
                case VISIBLE -> where.append(" AND p.active = true");
                case OCULTA -> where.append(" AND p.active = false");
                // Vencida mira la vigencia, no la visibilidad: una promoción caducada puede
                // seguir publicada, y encontrarla es justo para lo que sirve este filtro.
                case VENCIDA -> where.append(" AND p.valid_until < CURRENT_DATE");
            }
        }

        if (filter.source() != null) {
            where.append(" AND p.source = :origen");
            params.put("origen", filter.source().name());
        }

        if (filter.featuredInHome() != null) {
            List<Long> portada = filter.homeFeaturedIds();
            if (portada.isEmpty()) {
                // Sin promociones activas no hay portada: "en portada" no selecciona nada y
                // "fuera de portada" no excluye nada.
                if (filter.featuredInHome()) {
                    where.append(" AND 1 = 0");
                }
            } else {
                // Como mucho HomeFeaturedPolicy.SLOTS valores: se enumeran como parámetros con
                // nombre en vez de interpolar la lista, para no concatenar datos en el SQL.
                List<String> marcadores = new ArrayList<>(portada.size());
                for (int i = 0; i < portada.size(); i++) {
                    String nombre = "portada" + i;
                    marcadores.add(":" + nombre);
                    params.put(nombre, portada.get(i));
                }
                where.append(filter.featuredInHome() ? " AND p.id IN (" : " AND p.id NOT IN (")
                        .append(String.join(", ", marcadores))
                        .append(")");
            }
        }

        return new Criteria(where.toString(), params);
    }

    /**
     * Envuelve el término en comodines para el ILIKE, escapando antes los caracteres que LIKE
     * trata como patrón: sin esto, buscar "50%" o "sun_set" haría de comodín en vez de buscarse
     * literalmente. La barra invertida es el carácter de escape por defecto en PostgreSQL.
     */
    private static String toLikePattern(String search) {
        String escapado = search
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escapado + "%";
    }

    private record Criteria(String where, Map<String, Object> params) {
        void bind(Query query) {
            params.forEach(query::setParameter);
        }
    }

    // ------------------------------------------------------------------ resto

    @Override
    public long countActive() {
        return count("active = true");
    }

    @Override
    public Optional<Promotion> findPromotionById(Long id) {
        return findByIdOptional(id).map(PromotionPanacheEntity::toDomain);
    }

    @Override
    public Optional<Promotion> findBySlug(String slug) {
        return find("slug", slug).firstResultOptional().map(PromotionPanacheEntity::toDomain);
    }

    @Override
    public Optional<Promotion> findByFacebookPostId(String facebookPostId) {
        return find("facebookPostId", facebookPostId).firstResultOptional().map(PromotionPanacheEntity::toDomain);
    }

    @Override
    public Promotion save(Promotion promotion) {
        PromotionPanacheEntity entity = PromotionPanacheEntity.fromDomain(promotion);
        if (entity.id == null) {
            persist(entity);
            return entity.toDomain();
        } else {
            entity = getEntityManager().merge(entity);
            return entity.toDomain();
        }
    }

    @Override
    public void delete(Long id) {
        deleteById(id);
    }
}
