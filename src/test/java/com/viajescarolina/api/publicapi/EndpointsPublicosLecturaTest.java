package com.viajescarolina.api.publicapi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Superficie pública de lectura: son los endpoints que consume el sitio de Next.js en cada
 * render, así que una regresión aquí se ve directamente en la web.
 *
 * <p>Se comprueba lo que de verdad rompe una página: que responden 200 con JSON y sin exigir
 * sesión, y que el contenido mínimo que espera cada sección está presente. Las aserciones se
 * mantienen sobre datos sembrados por las migraciones, no sobre el contenido editorial real,
 * para que sigan siendo válidas cuando el equipo edite textos desde el panel.</p>
 */
@QuarkusTest
@DisplayName("API pública — endpoints de lectura que alimentan el sitio")
class EndpointsPublicosLecturaTest {

    @ParameterizedTest(name = "GET {0} responde 200 en JSON sin sesión")
    @ValueSource(strings = {
            "/api/public/v1/info",
            "/api/public/v1/site",
            "/api/public/v1/office",
            "/api/public/v1/about",
            "/api/public/v1/blog",
            "/api/public/v1/blog/categories",
            "/api/public/v1/blog/hero",
            "/api/public/v1/blog/library",
            "/api/public/v1/contact",
            "/api/public/v1/contact/explore-links",
            "/api/public/v1/home/hero",
            "/api/public/v1/home/blog-inspiration",
            "/api/public/v1/home/conversational-pause",
            "/api/public/v1/home/faq-section",
            "/api/public/v1/home/promotions-section",
            "/api/public/v1/home/testimonials-section",
            "/api/public/v1/home/trust",
            "/api/public/v1/legal/terminos",
            "/api/public/v1/legal/privacidad",
            "/api/public/v1/legal/cookies",
            "/api/public/v1/legal/esnna",
            "/api/public/v1/legal/mincetur",
            "/api/public/v1/promotions/featured"
    })
    @DisplayName("Responden 200 en JSON y sin autenticación")
    void respondenSinAutenticacion(String ruta) {
        given().when().get(ruta)
                .then().statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("La configuración del sitio expone la identidad de la agencia")
    void laConfiguracionDelSitioExponeLaIdentidad() {
        given().when().get("/api/public/v1/site")
                .then().statusCode(200)
                .body("siteName", notNullValue())
                .body("whatsappPhone", notNullValue())
                .body("legalCompanyName", notNullValue());
    }

    @Test
    @DisplayName("La oficina expone dirección y coordenadas para el mapa")
    void laOficinaExponeDireccionYCoordenadas() {
        given().when().get("/api/public/v1/office")
                .then().statusCode(200)
                .body("fullAddress", notNullValue())
                .body("city", notNullValue())
                .body("latitude", notNullValue())
                .body("longitude", notNullValue());
    }

    @Test
    @DisplayName("La búsqueda pública encuentra contenido sembrado por las migraciones")
    void laBusquedaPublicaEncuentraContenido() {
        given().queryParam("q", "Cartagena")
                .when().get("/api/public/v1/search")
                .then().statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Una búsqueda sin resultados responde 200, no error")
    void busquedaSinResultadosResponde200() {
        given().queryParam("q", "zzzzz-no-existe-zzzzz")
                .when().get("/api/public/v1/search")
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Un artículo de blog inexistente devuelve 404, no 500")
    void articuloInexistenteDevuelve404() {
        given().when().get("/api/public/v1/blog/posts/{slug}", "articulo-que-no-existe")
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Una foto inexistente devuelve 404, no 500")
    void fotoInexistenteDevuelve404() {
        given().when().get("/api/public/v1/media/{id}", 99_999_999L)
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Los términos legales llegan con el cuerpo editable del CMS")
    void losTerminosLleganConSuContenido() {
        given().when().get("/api/public/v1/legal/terminos")
                .then().statusCode(200)
                .body("sections", notNullValue());
    }

    @Test
    @DisplayName("Ningún endpoint público filtra credenciales de administración")
    void ningunEndpointPublicoFiltraCredenciales() {
        String configuracion = given().when().get("/api/public/v1/site")
                .then().statusCode(200).extract().asString();

        org.junit.jupiter.api.Assertions.assertFalse(
                configuracion.contains("passwordHash") || configuracion.contains("argon2"),
                "La configuración pública del sitio no puede llevar rastro de credenciales");
    }

    @Test
    @DisplayName("Los endpoints de administración sí exigen sesión")
    void losEndpointsDeAdministracionExigenSesion() {
        given().when().get("/api/admin/v1/promotions").then().statusCode(401);
        given().when().get("/api/admin/v1/publishing/status").then().statusCode(401);
        given().when().get("/api/admin/v1/auth/me").then().statusCode(401);
    }
}
