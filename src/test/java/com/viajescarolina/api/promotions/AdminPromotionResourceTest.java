package com.viajescarolina.api.promotions;

import com.viajescarolina.api.support.DatosDePrueba;
import com.viajescarolina.api.support.SesionAdmin;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Gestión de promociones desde el panel: el guard del pool mínimo de promociones activas,
 * la edición con PUT y sus invariantes de preservación, y el control de acceso.
 *
 * <p><b>Aislamiento.</b> Cada test parte de {@link DatosDePrueba#reiniciarPromociones()}, que
 * borra las promociones creadas por la suite y desactiva las que siembran las migraciones. Así
 * el número de activas es exactamente el que cada test decide crear y ninguno depende del orden
 * de ejecución ni de lo que dejó el anterior. Todo ocurre sobre el PostgreSQL efímero de Dev
 * Services, nunca sobre la base de desarrollo.</p>
 */
@QuarkusTest
@DisplayName("Promociones (admin) — pool mínimo de 3 activas, edición y permisos")
class AdminPromotionResourceTest {

    private static final String BASE = "/api/admin/v1/promotions";

    /** Mismo valor que SetPromotionActiveUseCase / DeletePromotionUseCase.MIN_ACTIVE_POOL. */
    private static final int MINIMO_ACTIVAS = 3;

    /** Mismo valor que HomeFeaturedPolicy.SLOTS: los huecos del bloque de promociones de Inicio. */
    private static final int HUECOS_DE_PORTADA = 3;

    @Inject
    DatosDePrueba datos;

    @BeforeEach
    void prepararEscenario() {
        datos.reiniciarPromociones();
    }

    @AfterEach
    void limpiar() {
        datos.limpiarPromocionesDePrueba();
        // Las creadas vía POST no llevan el prefijo de la suite (el slug lo genera el dominio
        // a partir del título), así que se borran por su slug conocido.
        datos.limpiarPromocionesPorSlug("cusco-imperial-en-5-dias%");
    }

    private RequestSpecification comoAdmin() {
        return given()
                .cookie(SesionAdmin.COOKIE, SesionAdmin.token(datos))
                .contentType(ContentType.JSON);
    }

    /** Formulario válido de promoción; los tests sobreescriben solo lo que les interesa. */
    private static Map<String, Object> formulario() {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("title", "Cusco imperial en 5 días");
        cuerpo.put("destination", "Cusco, Perú");
        cuerpo.put("summary", "Machu Picchu, Valle Sagrado y ciudad imperial con guía en español.");
        cuerpo.put("priceUsd", 899.00);
        cuerpo.put("pricePen", 3350.00);
        cuerpo.put("durationDays", 5);
        cuerpo.put("durationNights", 4);
        cuerpo.put("departureCity", "Arequipa");
        cuerpo.put("validFrom", "2026-09-01");
        cuerpo.put("validUntil", "2026-12-31");
        cuerpo.put("inclusions", List.of("Vuelos", "Hotel 4*"));
        cuerpo.put("exclusions", List.of("Propinas"));
        cuerpo.put("whatsappMessageTemplate", "Hola, me interesa Cusco imperial");
        return cuerpo;
    }

    // =====================================================================================
    // Guard del pool mínimo de activas — ocultar
    // =====================================================================================

    @Nested
    @DisplayName("Guard de 3 activas al ocultar (PATCH /{id}/active)")
    class OcultarPromocion {

        @Test
        @DisplayName("Con 4 activas se puede ocultar una: quedan 3")
        void conCuatroActivasSePuedeOcultarUna() {
            List<Long> ids = datos.crearPromocionesActivas(4);

            comoAdmin().body(Map.of("active", false))
                    .when().patch(BASE + "/{id}/active", ids.get(0))
                    .then().statusCode(200)
                    .body("active", is(false));

            assertEquals(MINIMO_ACTIVAS, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Con exactamente 3 activas, ocultar devuelve 409 y la promoción sigue visible")
        void conTresActivasOcultarDevuelve409() {
            List<Long> ids = datos.crearPromocionesActivas(MINIMO_ACTIVAS);

            comoAdmin().body(Map.of("active", false))
                    .when().patch(BASE + "/{id}/active", ids.get(0))
                    .then().statusCode(409);

            assertEquals(MINIMO_ACTIVAS, datos.contarPromocionesActivas(),
                    "El 409 debe dejar el estado intacto: Inicio necesita 3 promociones que mostrar");
            assertEquals(Boolean.TRUE, datos.leerCampoPromocion(ids.get(0), "active"));
        }

        @Test
        @DisplayName("Volver a mostrar una promoción oculta siempre se permite")
        void mostrarUnaOcultaSiemprreSePermite() {
            datos.crearPromocionesActivas(MINIMO_ACTIVAS);
            Long oculta = datos.crearPromocion("oculta", false);

            comoAdmin().body(Map.of("active", true))
                    .when().patch(BASE + "/{id}/active", oculta)
                    .then().statusCode(200)
                    .body("active", is(true));

            assertEquals(MINIMO_ACTIVAS + 1, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Ocultar una que ya estaba oculta no dispara el guard")
        void ocultarUnaYaOcultaNoDisparaElGuard() {
            // El guard solo mira promociones que HOY están activas: ocultar una ya oculta no
            // reduce el pool y debe pasar aunque estemos justo en el mínimo.
            datos.crearPromocionesActivas(MINIMO_ACTIVAS);
            Long oculta = datos.crearPromocion("ya-oculta", false);

            comoAdmin().body(Map.of("active", false))
                    .when().patch(BASE + "/{id}/active", oculta)
                    .then().statusCode(200)
                    .body("active", is(false));

            assertEquals(MINIMO_ACTIVAS, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Sobre un ID inexistente devuelve 404")
        void sobreIdInexistenteDevuelve404() {
            comoAdmin().body(Map.of("active", false))
                    .when().patch(BASE + "/{id}/active", 99_999_999L)
                    .then().statusCode(404);
        }

        @Test
        @DisplayName("Sin sesión administrativa devuelve 401")
        void sinSesionDevuelve401() {
            List<Long> ids = datos.crearPromocionesActivas(4);

            given().contentType(ContentType.JSON).body(Map.of("active", false))
                    .when().patch(BASE + "/{id}/active", ids.get(0))
                    .then().statusCode(401);

            assertEquals(4, datos.contarPromocionesActivas());
        }
    }

    // =====================================================================================
    // Guard del pool mínimo de activas — borrar
    // =====================================================================================

    @Nested
    @DisplayName("Guard de 3 activas al borrar (DELETE /{id})")
    class BorrarPromocion {

        @Test
        @DisplayName("Con 4 activas se puede borrar una")
        void conCuatroActivasSePuedeBorrar() {
            List<Long> ids = datos.crearPromocionesActivas(4);

            comoAdmin().when().delete(BASE + "/{id}", ids.get(0))
                    .then().statusCode(204);

            assertFalse(datos.existePromocion(ids.get(0)));
            assertEquals(MINIMO_ACTIVAS, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Con exactamente 3 activas, borrar devuelve 409 y la promoción sigue existiendo")
        void conTresActivasBorrarDevuelve409() {
            List<Long> ids = datos.crearPromocionesActivas(MINIMO_ACTIVAS);

            comoAdmin().when().delete(BASE + "/{id}", ids.get(0))
                    .then().statusCode(409);

            assertTrue(datos.existePromocion(ids.get(0)),
                    "El 409 debe abortar el borrado por completo, no borrar a medias");
            assertEquals(MINIMO_ACTIVAS, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Una promoción oculta se puede borrar aunque solo queden 3 activas")
        void unaOcultaSePuedeBorrarConElMinimoJusto() {
            datos.crearPromocionesActivas(MINIMO_ACTIVAS);
            Long oculta = datos.crearPromocion("borrable", false);

            comoAdmin().when().delete(BASE + "/{id}", oculta)
                    .then().statusCode(204);

            assertFalse(datos.existePromocion(oculta));
            assertEquals(MINIMO_ACTIVAS, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Sobre un ID inexistente devuelve 404")
        void sobreIdInexistenteDevuelve404() {
            comoAdmin().when().delete(BASE + "/{id}", 99_999_999L)
                    .then().statusCode(404);
        }

        @Test
        @DisplayName("Sin sesión administrativa devuelve 401 y no borra nada")
        void sinSesionDevuelve401() {
            List<Long> ids = datos.crearPromocionesActivas(4);

            given().when().delete(BASE + "/{id}", ids.get(0))
                    .then().statusCode(401);

            assertTrue(datos.existePromocion(ids.get(0)));
        }
    }

    // =====================================================================================
    // Edición (PUT)
    // =====================================================================================

    @Nested
    @DisplayName("Edición de una promoción (PUT /{id})")
    class EditarPromocion {

        @Test
        @DisplayName("Actualiza todos los campos editables del formulario")
        void actualizaLosCamposEditables() {
            Long id = datos.crearPromocion("editable", true);

            Map<String, Object> cuerpo = formulario();

            comoAdmin().body(cuerpo)
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(200)
                    .body("id", equalTo(id.intValue()))
                    .body("title", equalTo("Cusco imperial en 5 días"))
                    .body("destination", equalTo("Cusco, Perú"))
                    .body("priceUsd", equalTo(899.00f))
                    .body("pricePen", equalTo(3350.00f))
                    .body("durationDays", equalTo(5))
                    .body("durationNights", equalTo(4))
                    .body("departureCity", equalTo("Arequipa"))
                    .body("validFrom", equalTo("2026-09-01"))
                    .body("validUntil", equalTo("2026-12-31"))
                    .body("inclusions", hasItem("Hotel 4*"))
                    .body("exclusions", hasItem("Propinas"))
                    .body("whatsappMessageTemplate", equalTo("Hola, me interesa Cusco imperial"));
        }

        @Test
        @DisplayName("Preserva slug, visibilidad, origen, el post de Facebook y la fecha de alta")
        void preservaLoQueNoSeEdita() {
            // Promoción con rastro completo: vino de Facebook, está oculta y su alta es de 2020.
            Long id = datos.crearPromocion(
                    "con-rastro", false, "FACEBOOK", "108156039053235_999", "https://facebook.com/post/999", null);

            Object slugOriginal = datos.leerCampoPromocion(id, "slug");
            Object altaOriginal = datos.leerCampoPromocion(id, "created_at");

            comoAdmin().body(formulario())
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(200)
                    // El slug es la URL pública ya compartida: cambiar el título no debe regenerarlo.
                    .body("slug", equalTo(slugOriginal))
                    // Editar contenido no publica ni oculta: la visibilidad es de PATCH /active.
                    .body("active", is(false))
                    // Editar no republica en Facebook: el rastro sigue apuntando al post original.
                    .body("source", equalTo("FACEBOOK"))
                    .body("facebookPostId", equalTo("108156039053235_999"))
                    .body("facebookPermalinkUrl", equalTo("https://facebook.com/post/999"));

            assertEquals(slugOriginal, datos.leerCampoPromocion(id, "slug"));
            assertEquals(altaOriginal, datos.leerCampoPromocion(id, "created_at"),
                    "La fecha de alta no debe moverse al editar (columna updatable=false)");
            assertEquals(Boolean.FALSE, datos.leerCampoPromocion(id, "active"));
        }

        @Test
        @DisplayName("Una promoción activa sigue activa después de editarla")
        void unaActivaSigueActivaTrasEditar() {
            Long id = datos.crearPromocion("activa-editada", true);

            comoAdmin().body(formulario())
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(200)
                    .body("active", is(true));

            assertEquals(1, datos.contarPromocionesActivas());
        }

        @Test
        @DisplayName("Actualiza la fecha de modificación sin tocar la de alta")
        void mueveUpdatedAtPeroNoCreatedAt() {
            Long id = datos.crearPromocion("fechas", true);
            Object altaOriginal = datos.leerCampoPromocion(id, "created_at");
            Object modificacionOriginal = datos.leerCampoPromocion(id, "updated_at");

            comoAdmin().body(formulario()).when().put(BASE + "/{id}", id).then().statusCode(200);

            assertEquals(altaOriginal, datos.leerCampoPromocion(id, "created_at"));
            assertNotEquals(modificacionOriginal, datos.leerCampoPromocion(id, "updated_at"),
                    "Editar debe reflejarse en updated_at");
        }

        @Test
        @DisplayName("Sobre un ID inexistente devuelve 404")
        void sobreIdInexistenteDevuelve404() {
            comoAdmin().body(formulario())
                    .when().put(BASE + "/{id}", 99_999_999L)
                    .then().statusCode(404);
        }

        @Test
        @DisplayName("Con una foto destacada inexistente devuelve 404, no 500, y no altera la promoción")
        void conFotoInexistenteDevuelve404() {
            // La foto es una FK a media_asset: se valida antes de guardar para no reventar con
            // una violación de integridad (que llegaría al cliente como 500).
            Long id = datos.crearPromocion("foto-mala", true);
            Object tituloOriginal = datos.leerCampoPromocion(id, "title");

            Map<String, Object> cuerpo = formulario();
            cuerpo.put("featuredMediaId", 99_999_999L);

            comoAdmin().body(cuerpo)
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(404);

            assertEquals(tituloOriginal, datos.leerCampoPromocion(id, "title"),
                    "Si la foto no existe, la edición completa debe revertirse");
        }

        @Test
        @DisplayName("Con una foto destacada válida la asocia y devuelve su URL")
        void conFotoValidaLaAsocia() {
            Long id = datos.crearPromocion("foto-buena", true);
            Long fotoId = datos.idDeAlgunaFoto();

            Map<String, Object> cuerpo = formulario();
            cuerpo.put("featuredMediaId", fotoId);

            comoAdmin().body(cuerpo)
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(200)
                    .body("featuredMediaId", equalTo(fotoId.intValue()))
                    .body("featuredMediaUrl", notNullValue());
        }

        @Test
        @DisplayName("Se puede limpiar la foto destacada enviándola nula")
        void puedeLimpiarLaFotoDestacada() {
            Long fotoId = datos.idDeAlgunaFoto();
            Long id = datos.crearPromocion("con-foto", true, "MANUAL", null, null, fotoId);

            Map<String, Object> cuerpo = new HashMap<>(formulario());
            cuerpo.put("featuredMediaId", null);

            comoAdmin().body(cuerpo)
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(200);

            org.junit.jupiter.api.Assertions.assertNull(
                    datos.leerCampoPromocion(id, "featured_media_id"),
                    "Semántica PUT: un featuredMediaId nulo limpia la foto");
        }

        @Test
        @DisplayName("Con el formulario incompleto devuelve 400 por validación")
        void conFormularioIncompletoDevuelve400() {
            Long id = datos.crearPromocion("validacion", true);

            Map<String, Object> cuerpo = new HashMap<>(formulario());
            cuerpo.put("title", "   ");
            cuerpo.remove("priceUsd");

            comoAdmin().body(cuerpo)
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(400);
        }

        @Test
        @DisplayName("Sin sesión administrativa devuelve 401 y no modifica nada")
        void sinSesionDevuelve401() {
            Long id = datos.crearPromocion("protegida", true);
            Object tituloOriginal = datos.leerCampoPromocion(id, "title");

            given().contentType(ContentType.JSON).body(formulario())
                    .when().put(BASE + "/{id}", id)
                    .then().statusCode(401);

            assertEquals(tituloOriginal, datos.leerCampoPromocion(id, "title"));
        }

        @Test
        @DisplayName("La edición queda registrada en la bitácora")
        void laEdicionQuedaEnLaBitacora() {
            Long id = datos.crearPromocion("auditada", true);
            long antes = datos.contarBitacora("UPDATE_PROMOTION", "PROMOTION");

            comoAdmin().body(formulario()).when().put(BASE + "/{id}", id).then().statusCode(200);

            assertEquals(antes + 1, datos.contarBitacora("UPDATE_PROMOTION", "PROMOTION"),
                    "Toda edición de contenido publicado debe quedar trazada");
        }
    }

    // =====================================================================================
    // Alta y listado
    // =====================================================================================

    @Nested
    @DisplayName("Alta y listado")
    class AltaYListado {

        @Test
        @DisplayName("Crear devuelve 201, genera el slug desde el título y nace activa y MANUAL")
        void crearDevuelve201() {
            comoAdmin().body(formulario())
                    .when().post(BASE)
                    .then().statusCode(201)
                    .body("id", notNullValue())
                    .body("slug", equalTo("cusco-imperial-en-5-dias"))
                    .body("active", is(true))
                    .body("source", equalTo("MANUAL"));
        }

        @Test
        @DisplayName("Dos promociones con el mismo título reciben slugs distintos")
        void dosTitulosIgualesGeneranSlugsDistintos() {
            String primero = comoAdmin().body(formulario()).when().post(BASE)
                    .then().statusCode(201).extract().path("slug");
            String segundo = comoAdmin().body(formulario()).when().post(BASE)
                    .then().statusCode(201).extract().path("slug");

            assertNotEquals(primero, segundo, "El slug es UNIQUE: la segunda debe recibir sufijo");
            assertEquals("cusco-imperial-en-5-dias-2", segundo);
        }

        @Test
        @DisplayName("Crear sin sesión administrativa devuelve 401")
        void crearSinSesionDevuelve401() {
            given().contentType(ContentType.JSON).body(formulario())
                    .when().post(BASE)
                    .then().statusCode(401);
        }

        @Test
        @DisplayName("El listado del panel incluye tanto activas como ocultas")
        void elListadoIncluyeActivasYOcultas() {
            datos.crearPromocionesActivas(1);
            datos.crearPromocion("oculta-listado", false);

            List<Boolean> visibilidades = comoAdmin().when().get(BASE)
                    .then().statusCode(200)
                    .extract().jsonPath().getList("items.active", Boolean.class);

            assertTrue(visibilidades.contains(true), "El panel debe ver las promociones activas");
            assertTrue(visibilidades.contains(false), "El panel debe ver también las ocultas");
        }

        @Test
        @DisplayName("El listado sin sesión administrativa devuelve 401")
        void elListadoSinSesionDevuelve401() {
            given().when().get(BASE).then().statusCode(401);
        }
    }

    // =====================================================================================
    // Listado paginado en servidor
    // =====================================================================================

    /**
     * El listado del panel pagina, busca y filtra en la base de datos. Lo que se comprueba aquí
     * es que el recorte y la selección son de verdad del servidor: que una página trae solo su
     * página, que {@code total} cuenta lo filtrado mientras {@code summary} sigue describiendo
     * todo el catálogo, y que la marca de portada no se inventa un criterio propio.
     */
    @Nested
    @DisplayName("Listado paginado, búsqueda y filtros (GET ?page&size&search&status&source&featured)")
    class ListadoPaginado {

        /**
         * Palabra que no aparece en ninguna promoción sembrada por las migraciones: permite que
         * cada test se quede exactamente con las filas que él mismo creó, sin depender de cuántas
         * traiga la base de partida.
         */
        private static final String TERMINO = "Zanzibar";

        private JsonPath listar(String... parametros) {
            RequestSpecification peticion = comoAdmin();
            for (int i = 0; i < parametros.length; i += 2) {
                peticion.queryParam(parametros[i], parametros[i + 1]);
            }
            return peticion.when().get(BASE).then().statusCode(200).extract().jsonPath();
        }

        private List<Integer> ids(JsonPath respuesta) {
            return respuesta.getList("items.id", Integer.class);
        }

        // ------------------------------------------------------------------ paginación

        @Test
        @DisplayName("Devuelve solo la página pedida, no el catálogo entero")
        void devuelveSoloLaPaginaPedida() {
            datos.crearPromocionesActivas(7);
            long enCatalogo = datos.contarPromociones();

            JsonPath pagina = listar("page", "0", "size", "3");

            assertEquals(3, ids(pagina).size(), "size=3 debe devolver 3 filas, no las " + enCatalogo + " del catálogo");
            assertEquals(0, pagina.getInt("page"));
            assertEquals(3, pagina.getInt("size"));
            assertEquals(enCatalogo, pagina.getLong("total"),
                    "total cuenta todas las filas que cumplen los filtros, no las devueltas");
        }

        @Test
        @DisplayName("Páginas consecutivas no se solapan y recorren el catálogo en orden")
        void paginasConsecutivasNoSeSolapan() {
            datos.crearPromocionesActivas(7);

            List<Integer> primera = ids(listar("page", "0", "size", "3"));
            List<Integer> segunda = ids(listar("page", "1", "size", "3"));

            assertEquals(3, segunda.size());
            assertTrue(Collections.disjoint(primera, segunda),
                    "La página 1 no puede repetir filas de la página 0");

            // Recorrer el catálogo de página en página tiene que dar exactamente lo mismo que
            // pedirlo de una vez: mismo orden, sin saltarse ni repetir filas por el camino.
            List<Integer> recorrido = new ArrayList<>(primera);
            recorrido.addAll(segunda);
            assertEquals(ids(listar("page", "0", "size", "6")), recorrido,
                    "El recorrido de páginas debe seguir el orden del catálogo, sin saltos ni repeticiones");
        }

        @Test
        @DisplayName("Una página fuera de rango llega vacía, pero total y summary siguen contando")
        void paginaFueraDeRangoLlegaVacia() {
            datos.crearPromocionesActivas(3);
            long enCatalogo = datos.contarPromociones();

            JsonPath pagina = listar("page", "99", "size", "15");

            assertTrue(ids(pagina).isEmpty());
            assertEquals(enCatalogo, pagina.getLong("total"));
            assertEquals(enCatalogo, pagina.getLong("summary.total"));
        }

        @Test
        @DisplayName("Un size desmedido queda acotado: ?size=10000 no devuelve el catálogo entero")
        void unSizeDesmedidoQuedaAcotado() {
            // Por encima del techo de 100 para que el recorte se note en las filas, no solo en
            // el eco del parámetro: sin tope, este ?size=10000 sería "devuélvemelo todo".
            datos.crearPromocionesActivas(104);
            long enCatalogo = datos.contarPromociones();
            assertTrue(enCatalogo > 100, "El escenario necesita más filas que el techo de página");

            JsonPath pagina = listar("page", "0", "size", "10000");

            assertEquals(100, pagina.getInt("size"), "El size devuelto debe ser el realmente aplicado");
            assertEquals(100, ids(pagina).size(), "Nadie puede pedir el catálogo entero en una página");
            assertEquals(enCatalogo, pagina.getLong("total"));
        }

        // ------------------------------------------------------------------ total vs summary

        @Test
        @DisplayName("total refleja el filtro; summary sigue describiendo todo el catálogo")
        void totalReflejaElFiltroYSummaryNo() {
            datos.crearPromocionBuscable("busca-1", "QA " + TERMINO + " en velero", "Destino QA", "Resumen QA");
            datos.crearPromocionBuscable("busca-2", "QA playas", TERMINO + ", Tanzania", "Resumen QA");
            datos.crearPromocionesActivas(4);
            long enCatalogo = datos.contarPromociones();

            JsonPath filtrada = listar("search", TERMINO, "size", "15");

            assertEquals(2, filtrada.getLong("total"), "total solo cuenta lo que cumple el filtro");
            assertEquals(enCatalogo, filtrada.getLong("summary.total"),
                    "Las métricas de la cabecera describen todo el catálogo aunque haya un filtro puesto");
        }

        @Test
        @DisplayName("Las cuatro métricas de la cabecera cuadran con la base de datos")
        void lasMetricasDeLaCabeceraCuadranConLaBase() {
            datos.crearPromocionesActivas(4);
            datos.crearPromocion("resumen-oculta", false);
            datos.crearPromocionDeFacebook("resumen-fb-1", false, "QA post 1");
            datos.crearPromocionDeFacebook("resumen-fb-2", true, "QA post 2");

            JsonPath resumen = listar("size", "1");

            assertEquals(datos.contarPromociones(), resumen.getLong("summary.total"));
            assertEquals(datos.contarPromocionesConPermalinkDeFacebook(), resumen.getLong("summary.publishedOnFacebook"));
            assertEquals(datos.contarPromocionesOcultas(), resumen.getLong("summary.hidden"));
            assertEquals(HUECOS_DE_PORTADA, resumen.getLong("summary.featuredInHome"),
                    "Con 5 activas, la portada está llena");
        }

        @Test
        @DisplayName("Con menos activas que huecos, la portada cuenta solo las que hay")
        void conMenosActivasQueHuecosLaPortadaCuentaLasQueHay() {
            datos.crearPromocionesActivas(2);

            assertEquals(2, listar("size", "1").getLong("summary.featuredInHome"));
        }

        // ------------------------------------------------------------------ búsqueda

        @Test
        @DisplayName("La búsqueda encuentra por título, por destino y por resumen")
        void laBusquedaEncuentraPorTituloDestinoYResumen() {
            Long porTitulo = datos.crearPromocionBuscable(
                    "por-titulo", "QA " + TERMINO + " en velero", "Destino QA", "Resumen QA");
            Long porDestino = datos.crearPromocionBuscable(
                    "por-destino", "QA playas del este", TERMINO + ", Tanzania", "Resumen QA");
            Long porResumen = datos.crearPromocionBuscable(
                    "por-resumen", "QA islas", "Destino QA", "Siete noches en " + TERMINO + " con guía.");
            Long ajena = datos.crearPromocionBuscable(
                    "ajena", "QA Cusco imperial", "Cusco, Perú", "Nada que ver con la búsqueda.");

            List<Integer> encontradas = ids(listar("search", TERMINO, "size", "15"));

            assertEquals(
                    Set.of(porTitulo.intValue(), porDestino.intValue(), porResumen.intValue()),
                    Set.copyOf(encontradas));
            assertFalse(encontradas.contains(ajena.intValue()));
        }

        @Test
        @DisplayName("La búsqueda no distingue mayúsculas de minúsculas")
        void laBusquedaNoDistingueMayusculas() {
            Long id = datos.crearPromocionBuscable(
                    "mayusculas", "QA " + TERMINO.toUpperCase(Locale.ROOT), "Destino QA", "Resumen QA");

            assertEquals(List.of(id.intValue()), ids(listar("search", TERMINO.toLowerCase(Locale.ROOT))));
        }

        @Test
        @DisplayName("Los comodines de LIKE se buscan literalmente, no como patrón")
        void losComodinesDeLikeSeBuscanLiteralmente() {
            datos.crearPromocionBuscable("literal", "QA descuento del 50% en " + TERMINO, "Destino QA", "Resumen QA");
            datos.crearPromocionBuscable("otra", "QA " + TERMINO + " sin descuento", "Destino QA", "Resumen QA");

            // Un '%' sin escapar convertiría "50%" en "todo lo que empiece por 50".
            assertEquals(1, listar("search", "50%").getLong("total"));
        }

        // ------------------------------------------------------------------ filtros

        @Test
        @DisplayName("status=VISIBLE y status=OCULTA parten el catálogo por visibilidad")
        void statusParteElCatalogoPorVisibilidad() {
            Long visible = datos.crearPromocionBuscable("visible", "QA " + TERMINO + " visible", "Destino QA", "Resumen QA");
            Long oculta = datos.crearPromocionCompleta("oculta", false, "QA " + TERMINO + " oculta",
                    "Destino QA", "Resumen QA", LocalDate.now().plusDays(90), "MANUAL", null, null, null);

            assertEquals(List.of(visible.intValue()), ids(listar("search", TERMINO, "status", "VISIBLE")));
            assertEquals(List.of(oculta.intValue()), ids(listar("search", TERMINO, "status", "OCULTA")));
            assertEquals(2, listar("search", TERMINO, "status", "").getLong("total"),
                    "Sin valor, el filtro no filtra");
        }

        @Test
        @DisplayName("status=VENCIDA mira la vigencia, no la visibilidad")
        void statusVencidaMiraLaVigencia() {
            Long vencidaVisible = datos.crearPromocionVencida("vencida-visible", true, "QA " + TERMINO + " caducada");
            Long vencidaOculta = datos.crearPromocionVencida("vencida-oculta", false, "QA " + TERMINO + " caducada y oculta");
            Long vigente = datos.crearPromocionBuscable("vigente", "QA " + TERMINO + " vigente", "Destino QA", "Resumen QA");

            List<Integer> vencidas = ids(listar("search", TERMINO, "status", "VENCIDA"));

            assertEquals(Set.of(vencidaVisible.intValue(), vencidaOculta.intValue()), Set.copyOf(vencidas),
                    "Una promoción vencida sigue estando vencida aunque siga publicada");
            assertFalse(vencidas.contains(vigente.intValue()));
        }

        @Test
        @DisplayName("source separa lo dado de alta en el panel de lo heredado de Facebook")
        void sourceSeparaManualDeFacebook() {
            Long manual = datos.crearPromocionBuscable("manual", "QA " + TERMINO + " manual", "Destino QA", "Resumen QA");
            Long deFacebook = datos.crearPromocionDeFacebook("fb", true, "QA " + TERMINO + " de Facebook");

            assertEquals(List.of(manual.intValue()), ids(listar("search", TERMINO, "source", "MANUAL")));
            assertEquals(List.of(deFacebook.intValue()), ids(listar("search", TERMINO, "source", "FACEBOOK")));
        }

        @Test
        @DisplayName("featured=SI devuelve la portada y featured=NO todo lo demás")
        void featuredParteElCatalogoPorLaPortada() {
            List<Long> activas = datos.crearPromocionesActivas(5);
            // Comparten created_at, así que la portada la ocupan las tres de ID más alto.
            Set<Integer> enPortada = activas.subList(2, 5).stream().map(Long::intValue).collect(Collectors.toSet());
            long enCatalogo = datos.contarPromociones();

            JsonPath si = listar("featured", "SI", "size", "50");
            assertEquals(enPortada, Set.copyOf(ids(si)));
            assertEquals(HUECOS_DE_PORTADA, si.getLong("total"));

            JsonPath no = listar("featured", "NO", "size", "50");
            assertTrue(Collections.disjoint(ids(no), enPortada));
            assertEquals(enCatalogo - HUECOS_DE_PORTADA, no.getLong("total"));
        }

        @Test
        @DisplayName("Los filtros se combinan entre sí")
        void losFiltrosSeCombinan() {
            datos.crearPromocionDeFacebook("combi-fb-visible", true, "QA " + TERMINO + " fb visible");
            Long objetivo = datos.crearPromocionDeFacebook("combi-fb-oculta", false, "QA " + TERMINO + " fb oculta");
            datos.crearPromocionCompleta("combi-manual-oculta", false, "QA " + TERMINO + " manual oculta",
                    "Destino QA", "Resumen QA", LocalDate.now().plusDays(90), "MANUAL", null, null, null);

            assertEquals(List.of(objetivo.intValue()),
                    ids(listar("search", TERMINO, "source", "FACEBOOK", "status", "OCULTA")));
        }

        @Test
        @DisplayName("Un valor no reconocido en un filtro devuelve 400, no un listado cualquiera")
        void unValorNoReconocidoDevuelve400() {
            comoAdmin().queryParam("status", "TODAS").when().get(BASE).then().statusCode(400);
            comoAdmin().queryParam("source", "INSTAGRAM").when().get(BASE).then().statusCode(400);
            comoAdmin().queryParam("featured", "QUIZAS").when().get(BASE).then().statusCode(400);
        }

        // ------------------------------------------------------------------ marca de portada

        @Test
        @DisplayName("featuredInHome marca exactamente las 3 que Inicio muestra, ni una más")
        void featuredInHomeMarcaExactamenteLasDeLaPortada() {
            List<Long> activas = datos.crearPromocionesActivas(5);
            datos.crearPromocion("marca-oculta", false);
            Set<Integer> esperadas = activas.subList(2, 5).stream().map(Long::intValue).collect(Collectors.toSet());

            JsonPath pagina = listar("size", "50");
            List<Integer> marcadas = pagina.getList("items.findAll { it.featuredInHome == true }.id", Integer.class);
            List<Integer> sinMarcar = pagina.getList("items.findAll { it.featuredInHome == false }.id", Integer.class);

            assertEquals(esperadas, Set.copyOf(marcadas));
            assertEquals(ids(pagina).size() - HUECOS_DE_PORTADA, sinMarcar.size(),
                    "Toda fila del listado debe llevar la marca resuelta, a true o a false");
        }

        @Test
        @DisplayName("La marca del panel coincide con lo que el endpoint público pone en Inicio")
        void laMarcaCoincideConLaPortadaPublica() {
            // La prueba de que "estar en portada" es un solo criterio y no dos parecidos: si el
            // listado lo dedujera por su cuenta, aquí se vería la discrepancia.
            datos.crearPromocionesActivas(6);

            List<Integer> segunElPanel = listar("size", "50")
                    .getList("items.findAll { it.featuredInHome == true }.id", Integer.class);
            List<Integer> segunInicio = given().when().get("/api/public/v1/promotions/featured")
                    .then().statusCode(200).extract().jsonPath().getList("id", Integer.class);

            assertEquals(segunInicio, segunElPanel,
                    "El panel no puede marcar 'en portada' filas distintas de las que Inicio muestra");
        }

        @Test
        @DisplayName("El filtro featured=SI selecciona justo las filas que llevan la marca")
        void elFiltroSeleccionaJustoLasMarcadas() {
            datos.crearPromocionesActivas(5);

            List<Integer> marcadas = listar("size", "50")
                    .getList("items.findAll { it.featuredInHome == true }.id", Integer.class);
            List<Integer> filtradas = ids(listar("featured", "SI", "size", "50"));

            assertEquals(marcadas, filtradas, "La marca y el filtro deben salir del mismo cálculo");
        }
    }
}
