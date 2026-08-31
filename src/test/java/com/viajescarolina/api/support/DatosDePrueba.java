package com.viajescarolina.api.support;

import com.viajescarolina.api.auth.domain.PasswordHasher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Utilidades de preparación y limpieza de datos para la suite.
 *
 * <p>Trabaja con SQL nativo a propósito: las pruebas no deben montar su escenario con los
 * mismos casos de uso que están verificando (si {@code save()} tuviera un fallo, el test lo
 * heredaría en el setup y no lo detectaría).</p>
 *
 * <p>Todas las promociones que crea llevan el prefijo {@link #PREFIJO} en el slug, de modo que
 * {@link #limpiarPromocionesDePrueba()} pueda borrar exactamente lo suyo. Aun así, la base es
 * un contenedor efímero (ver {@code AislamientoBaseDatosTest}): la limpieza existe para que los
 * tests no dependan del orden de ejecución, no para proteger datos reales.</p>
 */
@ApplicationScoped
public class DatosDePrueba {

    /** Prefijo de slug que identifica lo creado por la suite. */
    public static final String PREFIJO = "qa-";

    @Inject
    EntityManager em;

    @Inject
    PasswordHasher hasher;

    // ---------------------------------------------------------------- usuarios

    /**
     * Fija una contraseña conocida para un usuario administrativo sembrado por las migraciones.
     *
     * <p>Hace falta porque V14/V15 siembran hashes Argon2id cuyas contraseñas en claro se
     * generaron aleatoriamente y se entregaron fuera del repositorio: nadie —ni la suite— puede
     * deducirlas. La contraseña de desarrollo {@code admin123#} solo funciona contra
     * {@code vc-postgres} porque allí fue rotada a mano. Sobre la base efímera de tests hay que
     * establecerla explícitamente, y se hace con el mismo {@link PasswordHasher} de producción
     * para que el hash guardado tenga exactamente el formato que el login espera leer.</p>
     */
    @Transactional
    public void establecerClave(String username, String claveEnClaro) {
        em.createNativeQuery("UPDATE admin_user SET password_hash = :h WHERE username = :u")
                .setParameter("h", hasher.hash(claveEnClaro))
                .setParameter("u", username)
                .executeUpdate();
    }

    @Transactional
    public void marcarUsuarioActivo(String username, boolean activo) {
        em.createNativeQuery("UPDATE admin_user SET active = :a WHERE username = :u")
                .setParameter("a", activo)
                .setParameter("u", username)
                .executeUpdate();
    }

    // ---------------------------------------------------------------- promociones

    /**
     * Deja la tabla de promociones en un estado conocido: sin promociones de prueba previas y
     * con TODAS las promociones sembradas por las migraciones desactivadas, de forma que el
     * conteo de activas sea exactamente el que cada test cree a continuación.
     */
    @Transactional
    public void reiniciarPromociones() {
        em.createNativeQuery("DELETE FROM promotion WHERE slug LIKE :p")
                .setParameter("p", PREFIJO + "%")
                .executeUpdate();
        em.createNativeQuery("UPDATE promotion SET active = false").executeUpdate();
    }

    /**
     * Crea {@code cuantas} promociones activas de prueba y devuelve sus IDs en orden de
     * creación. Se usa para colocar el pool de activas justo por encima o justo en el mínimo
     * de 3 que protege el guard de promociones.
     */
    @Transactional
    public List<Long> crearPromocionesActivas(int cuantas) {
        return java.util.stream.IntStream.rangeClosed(1, cuantas)
                .mapToObj(i -> crearPromocion("activa-" + i, true))
                .toList();
    }

    /** Crea una promoción de prueba con datos completos y devuelve su ID. */
    @Transactional
    public Long crearPromocion(String sufijo, boolean activa) {
        return crearPromocion(sufijo, activa, "MANUAL", null, null, null);
    }

    /**
     * Crea una promoción de prueba controlando origen y rastro de Facebook: es lo que permite
     * comprobar después que una edición NO los pisa.
     */
    @Transactional
    public Long crearPromocion(String sufijo,
                               boolean activa,
                               String source,
                               String facebookPostId,
                               String facebookPermalinkUrl,
                               Long featuredMediaId) {
        return crearPromocionCompleta(sufijo, activa, "QA " + sufijo, "Destino QA",
                "Promoción creada por la suite automatizada de Viajes Carolina.",
                LocalDate.now().plusDays(90), source, facebookPostId, facebookPermalinkUrl, featuredMediaId);
    }

    /**
     * Crea una promoción de prueba con control total sobre TODO lo que miran los filtros del
     * listado del panel: el texto que se busca (título, destino, resumen), la vigencia (que
     * decide si está VENCIDA), el origen y el rastro de Facebook.
     *
     * <p>La fecha de alta es fija y antigua para todas, así que el orden del catálogo
     * ({@code created_at DESC, id DESC}) queda decidido por el ID: la última creada es la
     * primera de la lista, y la portada la ocupan las tres activas con ID más alto.</p>
     */
    @Transactional
    public Long crearPromocionCompleta(String sufijo,
                                       boolean activa,
                                       String titulo,
                                       String destino,
                                       String resumen,
                                       LocalDate validUntil,
                                       String source,
                                       String facebookPostId,
                                       String facebookPermalinkUrl,
                                       Long featuredMediaId) {
        String slug = PREFIJO + sufijo;
        Object id = em.createNativeQuery("""
                        INSERT INTO promotion (
                            slug, title, destination, summary, price_usd, price_pen,
                            duration_days, duration_nights, departure_city,
                            valid_from, valid_until, featured_media_id,
                            inclusions, exclusions, whatsapp_message_template,
                            active, created_at, updated_at, source,
                            facebook_post_id, facebook_permalink_url
                        ) VALUES (
                            :slug, :title, :destino, :resumen, 999.00, 3700.00,
                            5, 4, 'Lima',
                            :vigenciaDesde, :vigenciaHasta, :media,
                            '["Vuelos"]'::jsonb, '["Propinas"]'::jsonb, 'Hola, quiero información',
                            :activa, :creada, now(), :source,
                            :fbId, :fbUrl
                        ) RETURNING id
                        """)
                .setParameter("slug", slug)
                .setParameter("title", titulo)
                .setParameter("destino", destino)
                .setParameter("resumen", resumen)
                // valid_from nunca puede ser posterior a valid_until, ni siquiera en una vencida.
                .setParameter("vigenciaDesde", java.sql.Date.valueOf(validUntil.minusDays(30)))
                .setParameter("vigenciaHasta", java.sql.Date.valueOf(validUntil))
                .setParameter("media", featuredMediaId)
                .setParameter("activa", activa)
                // Fecha de alta fija y antigua: sirve para comprobar que una edición NO la mueve.
                .setParameter("creada", java.sql.Timestamp.from(Instant.parse("2020-01-15T10:30:00Z")))
                .setParameter("source", source)
                .setParameter("fbId", facebookPostId)
                .setParameter("fbUrl", facebookPermalinkUrl)
                .getSingleResult();
        return ((Number) id).longValue();
    }

    /** Promoción activa cuyo texto controlan los tests, para probar la búsqueda del listado. */
    @Transactional
    public Long crearPromocionBuscable(String sufijo, String titulo, String destino, String resumen) {
        return crearPromocionCompleta(sufijo, true, titulo, destino, resumen,
                LocalDate.now().plusDays(90), "MANUAL", null, null, null);
    }

    /** Promoción cuya vigencia ya pasó: es lo que el filtro {@code status=VENCIDA} debe encontrar. */
    @Transactional
    public Long crearPromocionVencida(String sufijo, boolean activa, String titulo) {
        return crearPromocionCompleta(sufijo, activa, titulo, "Destino QA",
                "Promoción vencida creada por la suite automatizada.",
                LocalDate.now().minusDays(30), "MANUAL", null, null, null);
    }

    /** Promoción de origen FACEBOOK, con su post ya publicado (permalink incluido). */
    @Transactional
    public Long crearPromocionDeFacebook(String sufijo, boolean activa, String titulo) {
        return crearPromocionCompleta(sufijo, activa, titulo, "Destino QA",
                "Promoción importada de la Página por la suite automatizada.",
                LocalDate.now().plusDays(90), "FACEBOOK",
                "qa-post-" + sufijo, "https://facebook.com/post/qa-" + sufijo, null);
    }

    @Transactional
    public void limpiarPromocionesDePrueba() {
        limpiarPromocionesPorSlug(PREFIJO + "%");
    }

    /** Borra por patrón de slug lo que crearon los tests a través del propio API. */
    @Transactional
    public void limpiarPromocionesPorSlug(String patronSlug) {
        em.createNativeQuery("DELETE FROM promotion WHERE slug LIKE :p")
                .setParameter("p", patronSlug)
                .executeUpdate();
    }

    public long contarPromocionesActivas() {
        return contar("SELECT count(*) FROM promotion WHERE active = true");
    }

    /** Todas las promociones de la base, incluidas las que siembran las migraciones. */
    public long contarPromociones() {
        return contar("SELECT count(*) FROM promotion");
    }

    /** Las que tienen post publicado en la Página (métrica "publishedOnFacebook" de la cabecera). */
    public long contarPromocionesConPermalinkDeFacebook() {
        return contar("SELECT count(*) FROM promotion WHERE facebook_permalink_url IS NOT NULL");
    }

    /** Las ocultas (métrica "hidden" de la cabecera). */
    public long contarPromocionesOcultas() {
        return contar("SELECT count(*) FROM promotion WHERE active = false");
    }

    private long contar(String sql) {
        return ((Number) em.createNativeQuery(sql).getSingleResult()).longValue();
    }

    public boolean existePromocion(Long id) {
        Object n = em.createNativeQuery("SELECT count(*) FROM promotion WHERE id = :id")
                .setParameter("id", id)
                .getSingleResult();
        return ((Number) n).longValue() > 0;
    }

    /** Lee un campo suelto de una promoción sin pasar por el mapeo de producción. */
    public Object leerCampoPromocion(Long id, String columna) {
        List<?> filas = em.createNativeQuery("SELECT " + columna + " FROM promotion WHERE id = :id")
                .setParameter("id", id)
                .getResultList();
        return filas.isEmpty() ? null : filas.get(0);
    }

    /** ID de cualquier foto ya sembrada por las migraciones, para usar como foto destacada válida. */
    public Long idDeAlgunaFoto() {
        List<?> filas = em.createNativeQuery("SELECT id FROM media_asset ORDER BY id LIMIT 1").getResultList();
        return filas.isEmpty() ? null : ((Number) filas.get(0)).longValue();
    }

    // ---------------------------------------------------------------- bitácora

    @Transactional
    public void borrarBitacoraDe(String entityType) {
        em.createNativeQuery("DELETE FROM audit_log WHERE entity_type = :t")
                .setParameter("t", entityType)
                .executeUpdate();
    }

    /** Inserta un registro de publicación con fecha e operador controlados. */
    @Transactional
    public void registrarPublicacionEnBitacora(String usuario, Instant cuando, String detallesJson) {
        em.createNativeQuery("""
                        INSERT INTO audit_log (user_id, username, action, entity_type, entity_id, details_json, created_at)
                        VALUES (NULL, :usuario, 'PUBLISH_ON_DEMAND_ISR', 'PUBLISHING', 'ALL', CAST(:detalles AS jsonb), :cuando)
                        """)
                .setParameter("usuario", usuario)
                .setParameter("detalles", detallesJson)
                .setParameter("cuando", java.sql.Timestamp.from(cuando))
                .executeUpdate();
    }

    public long contarBitacora(String action, String entityType) {
        Object n = em.createNativeQuery(
                        "SELECT count(*) FROM audit_log WHERE action = :a AND entity_type = :t")
                .setParameter("a", action)
                .setParameter("t", entityType)
                .getSingleResult();
        return ((Number) n).longValue();
    }
}
