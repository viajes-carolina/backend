package com.viajescarolina.api.promotions;

import com.viajescarolina.api.support.DatosDePrueba;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Promociones destacadas de la portada: es el endpoint que alimenta la sección de Inicio del
 * sitio público, así que se comprueba que solo publica lo visible y que respeta el tope de 3.
 */
@QuarkusTest
@DisplayName("Promociones (público) — /featured alimenta la portada")
class PublicPromotionResourceTest {

    private static final String FEATURED = "/api/public/v1/promotions/featured";

    @Inject
    DatosDePrueba datos;

    @BeforeEach
    void prepararEscenario() {
        datos.reiniciarPromociones();
    }

    @AfterEach
    void limpiar() {
        datos.limpiarPromocionesDePrueba();
    }

    @Test
    @DisplayName("Es público: no exige sesión")
    void esPublico() {
        datos.crearPromocionesActivas(3);

        given().when().get(FEATURED)
                .then().statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Devuelve como máximo 3 promociones aunque haya más activas")
    void devuelveComoMaximoTres() {
        datos.crearPromocionesActivas(6);

        given().when().get(FEATURED)
                .then().statusCode(200)
                .body("$", hasSize(3));
    }

    @Test
    @DisplayName("Nunca expone promociones ocultas")
    void nuncaExponeOcultas() {
        List<Long> activas = datos.crearPromocionesActivas(3);
        Long oculta = datos.crearPromocion("oculta-portada", false);

        List<Integer> ids = given().when().get(FEATURED)
                .then().statusCode(200)
                .body("active", everyItem(is(true)))
                .extract().jsonPath().getList("id", Integer.class);

        assertFalse(ids.contains(oculta.intValue()),
                "Una promoción oculta en el panel no puede seguir apareciendo en la portada");
        assertTrue(ids.containsAll(activas.stream().map(Long::intValue).toList()));
    }

    @Test
    @DisplayName("Sin promociones activas devuelve una lista vacía, no un error")
    void sinActivasDevuelveListaVacia() {
        datos.crearPromocion("solo-oculta", false);

        given().when().get(FEATURED)
                .then().statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @DisplayName("Se sirve con cabecera de caché para el CDN y el navegador")
    void seSirveConCacheControl() {
        datos.crearPromocionesActivas(3);

        String cacheControl = given().when().get(FEATURED)
                .then().statusCode(200)
                .extract().header("Cache-Control");

        assertEquals("public, max-age=60, s-maxage=300", cacheControl);
    }

    @Test
    @DisplayName("Cada promoción llega con los campos que necesita la tarjeta de portada")
    void traeLosCamposDeLaTarjeta() {
        datos.crearPromocionesActivas(3);

        given().when().get(FEATURED)
                .then().statusCode(200)
                .body("[0].id", org.hamcrest.Matchers.notNullValue())
                .body("[0].slug", org.hamcrest.Matchers.notNullValue())
                .body("[0].title", org.hamcrest.Matchers.notNullValue())
                .body("[0].destination", org.hamcrest.Matchers.notNullValue())
                .body("[0].summary", org.hamcrest.Matchers.notNullValue())
                .body("[0].priceUsd", org.hamcrest.Matchers.notNullValue())
                .body("[0].durationDays", org.hamcrest.Matchers.notNullValue())
                // El punto focal siempre viaja informado (por defecto el centro), porque el
                // recorte de la foto en la tarjeta depende de él.
                .body("[0].featuredMediaFocalX", org.hamcrest.Matchers.notNullValue())
                .body("[0].featuredMediaFocalY", org.hamcrest.Matchers.notNullValue());
    }
}
