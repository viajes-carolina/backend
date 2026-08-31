package com.viajescarolina.api.publishing;

import com.viajescarolina.api.support.DatosDePrueba;
import com.viajescarolina.api.support.SesionAdmin;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Endpoint de estado de publicación del panel.
 *
 * <p>Complementa a {@code GetPublishingStatusUseCaseTest}: aquí se comprueba de extremo a
 * extremo que lo que sale por HTTP es lo que hay en la bitácora, incluida la omisión de
 * {@code publishedAt} cuando el sitio nunca se ha publicado (Jackson está configurado con
 * {@code serialization-inclusion=non-null}, así que el campo no debe aparecer en el JSON).</p>
 *
 * <p><b>Aislamiento.</b> Cada test limpia los registros de tipo PUBLISHING antes y después, para
 * no depender de lo que haya escrito otro test ni dejarle rastro al siguiente.</p>
 */
@QuarkusTest
@DisplayName("Publicación (admin) — GET /status refleja la bitácora real")
class AdminPublishingResourceTest {

    private static final String ESTADO = "/api/admin/v1/publishing/status";

    @Inject
    DatosDePrueba datos;

    @BeforeEach
    void limpiarBitacoraDePublicaciones() {
        datos.borrarBitacoraDe("PUBLISHING");
    }

    @AfterEach
    void dejarLaBitacoraComoEstaba() {
        datos.borrarBitacoraDe("PUBLISHING");
    }

    private Response consultarEstado() {
        return given().cookie(SesionAdmin.COOKIE, SesionAdmin.token(datos)).when().get(ESTADO);
    }

    @Test
    @DisplayName("Sin ninguna publicación registrada devuelve NEVER_PUBLISHED y omite la fecha")
    void sinPublicacionesDevuelveNeverPublished() {
        Response respuesta = consultarEstado();

        respuesta.then().statusCode(200)
                .body("status", equalTo("NEVER_PUBLISHED"))
                .body("publishedAt", nullValue())
                .body("triggeredBy", nullValue())
                .body("revalidatedTags", equalTo(java.util.List.of()))
                .body("message", containsString("nunca se ha publicado"));

        // El defecto original devolvía Instant.now() disfrazado de última publicación: el JSON
        // ni siquiera debe traer la clave.
        assertFalse(respuesta.asString().contains("publishedAt"),
                "No debe aparecer publishedAt cuando el sitio nunca se publicó: " + respuesta.asString());
    }

    @Test
    @DisplayName("Con una publicación registrada devuelve su fecha real y su operador")
    void conPublicacionDevuelveFechaRealYOperador() {
        Instant cuando = Instant.parse("2026-07-02T18:45:00Z");
        datos.registrarPublicacionEnBitacora("carolina", cuando,
                "{\"target\":\"ALL\",\"tags\":[\"promotions\"],\"webhookSucceeded\":true,\"webhookDetail\":\"200 OK\"}");

        consultarEstado()
                .then().statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("publishedAt", equalTo("2026-07-02T18:45:00Z"))
                .body("triggeredBy", equalTo("carolina"))
                .body("revalidatedTags", hasItem("promotions"));
    }

    @Test
    @DisplayName("Devuelve la ÚLTIMA publicación, no la primera")
    void devuelveLaUltimaPublicacion() {
        datos.registrarPublicacionEnBitacora("admin", Instant.parse("2026-06-01T10:00:00Z"),
                "{\"webhookSucceeded\":true}");
        datos.registrarPublicacionEnBitacora("carolina", Instant.parse("2026-07-20T08:30:00Z"),
                "{\"webhookSucceeded\":true}");

        consultarEstado()
                .then().statusCode(200)
                .body("publishedAt", equalTo("2026-07-20T08:30:00Z"))
                .body("triggeredBy", equalTo("carolina"));
    }

    @Test
    @DisplayName("Una publicación cuyo webhook falló se reporta como FAILED, no como éxito")
    void publicacionFallidaSeReportaComoFailed() {
        datos.registrarPublicacionEnBitacora("admin", Instant.parse("2026-07-21T12:00:00Z"),
                "{\"tags\":[\"home\"],\"webhookSucceeded\":false,\"webhookDetail\":\"connection refused\"}");

        consultarEstado()
                .then().statusCode(200)
                .body("status", equalTo("FAILED"))
                .body("publishedAt", equalTo("2026-07-21T12:00:00Z"))
                .body("message", containsString("desactualizado"));
    }

    @Test
    @DisplayName("Sin sesión administrativa devuelve 401")
    void sinSesionDevuelve401() {
        given().when().get(ESTADO).then().statusCode(401);
    }

    /**
     * Las promociones se publican en la PORTADA: el sitio público no tiene ninguna ruta
     * {@code /promociones}. Este objetivo enviaba «promotions» y «/promociones», que el
     * webhook traducía a dos rutas inexistentes, así que publicar «Promociones» no
     * actualizaba nada y el panel lo daba por bueno igualmente.
     *
     * <p>El webhook falla a propósito en el perfil de test (apunta a 127.0.0.1:9), pero
     * las rutas elegidas viajan en la respuesta tanto si tuvo éxito como si no, que es
     * justo lo que aquí se fija.</p>
     */
    @Test
    @DisplayName("Publicar «Promociones» revalida la portada, no una ruta que no existe")
    void publicarPromocionesRevalidaLaPortada() {
        publicar("PROMOTIONS")
                .then().statusCode(200)
                .body("revalidatedTags", hasItem("/"))
                .body("revalidatedTags", not(hasItem("/promociones")))
                .body("revalidatedTags", not(hasItem("promotions")));
    }

    /**
     * El objetivo ALL revalidaba «/», «/blog», «/nosotros», «/contacto» y «/reclamaciones»,
     * y se dejaba fuera las cinco páginas legales: cambiar el aviso de privacidad o la
     * constancia MINCETUR no se reflejaba nunca en el sitio.
     */
    @Test
    @DisplayName("Publicar «Todo» incluye también las páginas legales")
    void publicarTodoIncluyeLasPaginasLegales() {
        publicar("ALL")
                .then().statusCode(200)
                .body("revalidatedTags", hasItems(
                        "/", "/blog", "/nosotros", "/contacto", "/reclamaciones", "/buscar",
                        "/terminos", "/privacidad", "/cookies", "/compromiso-esnna", "/constancia-mincetur"));
    }

    /**
     * Ninguna etiqueta suelta: el webhook traduce todo lo que no empiece por «/» a
     * «/loquesea», así que «all» y «home» pedían revalidar /all y /home — rutas que no
     * existen y que se contaban como publicadas igualmente.
     */
    @Test
    @DisplayName("Solo se envían rutas reales, nunca etiquetas sueltas")
    void soloSeEnvianRutasReales() {
        for (String objetivo : new String[]{"ALL", "HOME", "PROMOTIONS", "BLOG", "ABOUT", "CONTACT"}) {
            java.util.List<String> rutas = publicar(objetivo).then().statusCode(200)
                    .extract().jsonPath().getList("revalidatedTags", String.class);
            rutas.forEach(ruta -> org.junit.jupiter.api.Assertions.assertTrue(
                    ruta.startsWith("/"),
                    "El objetivo " + objetivo + " envía «" + ruta + "», que no es una ruta del sitio."));
        }
    }

    private Response publicar(String target) {
        return given().cookie(SesionAdmin.COOKIE, SesionAdmin.token(datos))
                .contentType("application/json")
                .body("{\"target\":\"" + target + "\",\"reason\":\"test\"}")
                .when().post("/api/admin/v1/publishing/publish");
    }
}
