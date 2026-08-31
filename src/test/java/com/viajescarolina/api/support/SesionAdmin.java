package com.viajescarolina.api.support;

import io.restassured.http.ContentType;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Sesión administrativa reutilizable para los tests de los recursos protegidos.
 *
 * <p>Se autentica de verdad contra {@code /api/admin/v1/auth/login} en lugar de usar
 * {@code @TestSecurity}: así los tests de promociones y publicación recorren la misma cadena
 * que el panel real (cookie {@code vc_admin_jwt} → verificación del JWT → {@code @RolesAllowed}),
 * y un fallo en esa cadena se vería aquí y no solo en producción.</p>
 *
 * <p>El token se calcula una única vez por JVM de test porque el hash Argon2id del login cuesta
 * ~200 ms; el JWT no tiene estado en servidor, así que reutilizarlo es seguro.</p>
 */
public final class SesionAdmin {

    public static final String COOKIE = "vc_admin_jwt";
    public static final String USUARIO = "admin";
    public static final String CLAVE = "admin123#";

    private static String token;

    private SesionAdmin() {
    }

    public static synchronized String token(DatosDePrueba datos) {
        if (token == null) {
            // Las migraciones siembran un hash cuya contraseña en claro nadie conoce: la suite
            // fija la suya antes de poder iniciar sesión (ver DatosDePrueba.establecerClave).
            datos.establecerClave(USUARIO, CLAVE);
            datos.marcarUsuarioActivo(USUARIO, true);
            token = given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("usernameOrEmail", USUARIO, "password", CLAVE))
                    .when().post("/api/admin/v1/auth/login")
                    .then().statusCode(200)
                    .extract().path("token");
        }
        return token;
    }
}
