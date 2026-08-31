package com.viajescarolina.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viajescarolina.api.support.DatosDePrueba;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Autenticación del panel administrativo: emisión del JWT, atributos de la cookie de sesión,
 * duración con y sin "Mantener mi sesión", rechazo de credenciales inválidas y cierre de sesión.
 *
 * <p>Corre contra el PostgreSQL efímero de Dev Services con los usuarios que siembra la
 * migración V14 ({@code admin} / {@code admin123#}, rol SUPER_ADMIN).</p>
 */
@QuarkusTest
@DisplayName("Auth — login, cookie de sesión, rememberMe y logout del panel")
class AdminAuthResourceTest {

    private static final String LOGIN = "/api/admin/v1/auth/login";
    private static final String LOGOUT = "/api/admin/v1/auth/logout";
    private static final String ME = "/api/admin/v1/auth/me";
    private static final String COOKIE = "vc_admin_jwt";

    private static final String USUARIO = "admin";
    private static final String CLAVE = "admin123#";

    /** Sesión estándar: 1 hora (LoginAdminUseCase.SESSION_TTL_SECONDS). */
    private static final int UNA_HORA = 3600;
    /** "Mantener mi sesión": 30 días (LoginAdminUseCase.EXTENDED_SESSION_TTL_SECONDS). */
    private static final int TREINTA_DIAS = 2592000;

    private static final ObjectMapper JSON = new ObjectMapper();

    @Inject
    DatosDePrueba datos;

    /** URL base del servidor de test; hace falta para las peticiones que no pasan por RestAssured. */
    private static String baseUri() {
        return "http://localhost:" + io.restassured.RestAssured.port;
    }

    /**
     * Las migraciones siembran hashes Argon2id de contraseñas aleatorias que nadie conoce
     * (ver V14/V15), así que la suite fija su propia clave para {@code admin} antes de cada
     * prueba. Se usa la misma que en desarrollo para que el escenario sea reconocible.
     */
    @org.junit.jupiter.api.BeforeEach
    void prepararCredenciales() {
        datos.establecerClave(USUARIO, CLAVE);
        datos.marcarUsuarioActivo(USUARIO, true);
    }

    private Response iniciarSesion(Object cuerpo) {
        return given().contentType(ContentType.JSON).body(cuerpo).when().post(LOGIN);
    }

    private String tokenValido() {
        return iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE))
                .then().statusCode(200)
                .extract().path("token");
    }

    // =====================================================================================
    // Login correcto
    // =====================================================================================

    @Nested
    @DisplayName("Login con credenciales correctas")
    class LoginCorrecto {

        @Test
        @DisplayName("Devuelve 200 con el JWT y el perfil del usuario")
        void devuelve200ConTokenYPerfil() {
            iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE))
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("token", notNullValue())
                    .body("tokenType", equalTo("Bearer"))
                    .body("user.username", equalTo("admin"))
                    .body("user.role", equalTo("SUPER_ADMIN"))
                    .body("user.active", is(true))
                    // La respuesta nunca debe filtrar el hash de la contraseña.
                    .body("user.passwordHash", nullValue());
        }

        @Test
        @DisplayName("Emite la cookie vc_admin_jwt como HttpOnly, SameSite=Strict, Secure y de ámbito /")
        void emiteCookieDeSesionEndurecida() {
            io.restassured.http.Cookie cookie = iniciarSesion(
                    Map.of("usernameOrEmail", USUARIO, "password", CLAVE))
                    .then().statusCode(200)
                    .extract().detailedCookie(COOKIE);

            assertNotNull(cookie, "El login debe emitir la cookie de sesión " + COOKIE);
            assertTrue(cookie.isHttpOnly(),
                    "La cookie de sesión debe ser HttpOnly: sin eso, cualquier XSS del panel se lleva el JWT");
            assertEquals("Strict", cookie.getSameSite(),
                    "La cookie de sesión debe ser SameSite=Strict para cortar el CSRF sobre el panel");
            assertTrue(cookie.isSecured(),
                    "Fuera de %dev la cookie debe viajar solo por HTTPS (viajescarolina.security.cookie-secure=true)");
            assertEquals("/", cookie.getPath());
            assertFalse(cookie.getValue().isBlank(), "La cookie debe llevar el JWT emitido");
        }

        @Test
        @DisplayName("El valor de la cookie es exactamente el JWT devuelto en el cuerpo")
        void laCookieLlevaElMismoJwtQueElCuerpo() {
            Response respuesta = iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE));
            respuesta.then().statusCode(200);

            assertEquals(respuesta.path("token"), respuesta.getCookie(COOKIE),
                    "La cookie y el cuerpo deben transportar el mismo token; si divergen, el panel "
                            + "autentica con uno y el navegador envía otro");
        }

        @Test
        @DisplayName("El JWT lleva el usuario, el rol y una expiración coherente")
        void elJwtLlevaUsuarioRolYExpiracion() throws Exception {
            String token = tokenValido();
            JsonNode claims = leerClaims(token);

            assertEquals("admin", claims.get("upn").asText());
            assertEquals("SUPER_ADMIN", claims.get("groups").get(0).asText());
            assertEquals("https://viajescarolina.com/issuer", claims.get("iss").asText());

            long segundosDeVida = claims.get("exp").asLong() - Instant.now().getEpochSecond();
            assertTrue(Math.abs(segundosDeVida - UNA_HORA) <= 60,
                    "El JWT de una sesión estándar debe caducar en ~1 hora, pero caduca en "
                            + segundosDeVida + " s");
        }

        @Test
        @DisplayName("Se puede entrar con el email en lugar del nombre de usuario")
        void aceptaElEmailComoIdentificador() {
            iniciarSesion(Map.of("usernameOrEmail", "admin@viajescarolina.com", "password", CLAVE))
                    .then().statusCode(200)
                    .body("user.username", equalTo("admin"));
        }

        @Test
        @DisplayName("El identificador no distingue mayúsculas ni espacios sobrantes")
        void normalizaElIdentificador() {
            iniciarSesion(Map.of("usernameOrEmail", "  ADMIN  ", "password", CLAVE))
                    .then().statusCode(200)
                    .body("user.username", equalTo("admin"));
        }

        @Test
        @DisplayName("El login exitoso queda registrado en la bitácora")
        void elLoginExitosoQuedaEnLaBitacora() {
            long antes = datos.contarBitacora("LOGIN_SUCCESS", "AUTH");

            iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE))
                    .then().statusCode(200);

            assertEquals(antes + 1, datos.contarBitacora("LOGIN_SUCCESS", "AUTH"),
                    "Cada inicio de sesión debe dejar un LOGIN_SUCCESS en la bitácora");
        }
    }

    // =====================================================================================
    // rememberMe — "Mantener mi sesión"
    // =====================================================================================

    @Nested
    @DisplayName("rememberMe (\"Mantener mi sesión\")")
    class MantenerSesion {

        @Test
        @DisplayName("Sin marcar, la sesión dura 1 hora (3600 s)")
        void sinMarcarDuraUnaHora() {
            iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE, "rememberMe", false))
                    .then().statusCode(200)
                    .body("expiresInSeconds", equalTo(UNA_HORA));
        }

        @Test
        @DisplayName("Marcado, la sesión dura 30 días (2592000 s)")
        void marcadoDuraTreintaDias() {
            iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE, "rememberMe", true))
                    .then().statusCode(200)
                    .body("expiresInSeconds", equalTo(TREINTA_DIAS));
        }

        @Test
        @DisplayName("Si el cliente no envía el campo, se trata como sesión estándar de 1 hora")
        void ausenteEquivaleASesionEstandar() {
            // Contrato con clientes antiguos: rememberMe es opcional y null no debe extender nada.
            iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", CLAVE))
                    .then().statusCode(200)
                    .body("expiresInSeconds", equalTo(UNA_HORA));
        }

        @ParameterizedTest(name = "rememberMe={0} → cookie Max-Age {1} s")
        @CsvSource({"false," + UNA_HORA, "true," + TREINTA_DIAS})
        @DisplayName("El Max-Age de la cookie sigue exactamente al expiresInSeconds de la respuesta")
        void elMaxAgeDeLaCookieSigueAlExpiresInSeconds(boolean recordarme, int segundosEsperados) {
            Response respuesta = iniciarSesion(
                    Map.of("usernameOrEmail", USUARIO, "password", CLAVE, "rememberMe", recordarme));
            respuesta.then().statusCode(200);

            long expiresInSeconds = ((Number) respuesta.path("expiresInSeconds")).longValue();
            io.restassured.http.Cookie cookie = respuesta.detailedCookie(COOKIE);

            assertEquals(segundosEsperados, expiresInSeconds);
            assertEquals(segundosEsperados, cookie.getMaxAge(),
                    "El Max-Age de la cookie debe ir a la par del token; si se desincronizan, el "
                            + "navegador conserva una cookie ya caducada (o la tira antes de tiempo)");
        }

        @Test
        @DisplayName("Con rememberMe, el propio JWT también caduca a los 30 días")
        void conRememberMeElJwtTambienDuraTreintaDias() throws Exception {
            String token = iniciarSesion(
                    Map.of("usernameOrEmail", USUARIO, "password", CLAVE, "rememberMe", true))
                    .then().statusCode(200)
                    .extract().path("token");

            long segundosDeVida = leerClaims(token).get("exp").asLong() - Instant.now().getEpochSecond();
            assertTrue(Math.abs(segundosDeVida - TREINTA_DIAS) <= 60,
                    "El JWT extendido debe caducar en ~30 días, pero caduca en " + segundosDeVida + " s");
        }
    }

    // =====================================================================================
    // Credenciales inválidas
    // =====================================================================================

    @Nested
    @DisplayName("Credenciales inválidas")
    class CredencialesInvalidas {

        @Test
        @DisplayName("Contraseña incorrecta devuelve 401 y no emite cookie de sesión")
        void contrasenaIncorrectaDevuelve401SinCookie() {
            Response respuesta = iniciarSesion(
                    Map.of("usernameOrEmail", USUARIO, "password", "clave-que-no-es"));

            respuesta.then().statusCode(401);
            assertNull(respuesta.getCookie(COOKIE),
                    "Un login fallido jamás debe dejar una cookie de sesión en el navegador");
        }

        @Test
        @DisplayName("Usuario inexistente devuelve 401 (mismo trato que contraseña incorrecta)")
        void usuarioInexistenteDevuelve401() {
            // Mismo código para "no existe" y "clave mala": no se filtra qué usuarios existen.
            iniciarSesion(Map.of("usernameOrEmail", "fantasma", "password", CLAVE))
                    .then().statusCode(401);
        }

        @Test
        @DisplayName("Sin usuario o sin contraseña devuelve 400 por validación")
        void camposVaciosDevuelve400() {
            iniciarSesion(Map.of("usernameOrEmail", "", "password", ""))
                    .then().statusCode(400);
        }

        @Test
        @DisplayName("La respuesta de un login fallido no contiene ningún token")
        void elLoginFallidoNoDevuelveToken() {
            String cuerpo = iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", "otra-cosa"))
                    .then().statusCode(401)
                    .extract().asString();

            assertFalse(cuerpo.contains("token"),
                    "El 401 no debe devolver nada parecido a una credencial; devolvió: " + cuerpo);
        }

        @Test
        @DisplayName("Una cuenta desactivada devuelve 403 aunque la contraseña sea correcta")
        void cuentaDesactivadaDevuelve403() {
            datos.marcarUsuarioActivo("editor", false);
            datos.establecerClave("editor", CLAVE);
            try {
                iniciarSesion(Map.of("usernameOrEmail", "editor", "password", CLAVE))
                        .then().statusCode(403);
            } finally {
                datos.marcarUsuarioActivo("editor", true);
            }
        }

        @Test
        @DisplayName("Un login fallido queda registrado en la bitácora")
        void elLoginFallidoQuedaEnLaBitacora() {
            long antes = datos.contarBitacora("LOGIN_FAILED", "AUTH");

            iniciarSesion(Map.of("usernameOrEmail", USUARIO, "password", "clave-que-no-es"))
                    .then().statusCode(401);

            assertEquals(antes + 1, datos.contarBitacora("LOGIN_FAILED", "AUTH"),
                    "Un intento fallido debe quedar en la bitácora para poder auditar accesos");
        }

    }

    // =====================================================================================
    // Cierre de sesión
    // =====================================================================================

    @Nested
    @DisplayName("Logout")
    class CierreDeSesion {

        @Test
        @DisplayName("Con Content-Type: application/json cierra la sesión y caduca la cookie")
        void conJsonCierraLaSesion() {
            Response respuesta = given()
                    .contentType(ContentType.JSON)
                    .cookie(COOKIE, tokenValido())
                    .when().post(LOGOUT);

            respuesta.then().statusCode(200).body("status", equalTo("LOGGED_OUT"));
            assertEquals(0, respuesta.detailedCookie(COOKIE).getMaxAge(),
                    "El logout debe devolver la cookie con Max-Age=0 para que el navegador la borre");
            assertEquals("", respuesta.getCookie(COOKIE));
        }

        @Test
        @DisplayName("Sin cabecera Content-Type también cierra la sesión")
        void sinContentTypeCierraLaSesion() throws Exception {
            // Se usa el HttpClient del JDK y no RestAssured porque RestAssured añade por su
            // cuenta un Content-Type por defecto a todo POST: con él nunca se podría reproducir
            // el caso real de un `fetch` sin cuerpo, que es lo que hace el panel al cerrar sesión.
            // El recurso declara @Consumes(application/json) a nivel de clase, pero el método no
            // recibe cuerpo: sin cabecera Content-Type, RESTEasy lo acepta igual.
            java.net.http.HttpResponse<String> respuesta = peticionCruda(
                    java.net.http.HttpRequest.newBuilder()
                            .uri(java.net.URI.create(baseUri() + LOGOUT))
                            .header("Cookie", COOKIE + "=" + tokenValido())
                            .POST(java.net.http.HttpRequest.BodyPublishers.noBody()));

            assertEquals(200, respuesta.statusCode(),
                    "Un logout sin cuerpo ni Content-Type debe cerrar la sesión, no rebotar");
            assertTrue(respuesta.body().contains("LOGGED_OUT"));
            assertTrue(cabecerasSetCookie(respuesta).stream().anyMatch(c -> c.contains("Max-Age=0")),
                    "El logout debe devolver la cookie con Max-Age=0; cabeceras: "
                            + cabecerasSetCookie(respuesta));
        }

        @Test
        @DisplayName("Con un Content-Type distinto de JSON también cierra la sesión")
        void conContentTypeNoJsonCierraLaSesion() {
            // Regresión de auth-002. El recurso declara @Consumes(application/json) a nivel de
            // clase, y eso hacía que un logout con, por ejemplo, `text/plain` — lo que pone un
            // `fetch` con un body de texto — se rechazara con 415 SIN llegar al método: no se
            // emitía la cookie caducada y el token seguía autenticando. El panel daba la sesión
            // por cerrada mientras seguía viva. El endpoint no consume cuerpo: declara
            // @Consumes(MediaType.WILDCARD).
            String token = tokenValido();

            Response respuesta = given()
                    .contentType("text/plain")
                    .body("")
                    .cookie(COOKIE, token)
                    .when().post(LOGOUT);

            respuesta.then().statusCode(200);
            assertEquals(0, respuesta.detailedCookie(COOKIE).getMaxAge(),
                    "El logout debe caducar la cookie sea cual sea el Content-Type");
        }

        @Test
        @DisplayName("Sin sesión activa devuelve 401")
        void sinSesionDevuelve401() {
            given().contentType(ContentType.JSON).when().post(LOGOUT)
                    .then().statusCode(401);
        }

        @Test
        @DisplayName("Con una cookie manipulada devuelve 401")
        void conCookieManipuladaDevuelve401() {
            given().contentType(ContentType.JSON)
                    .cookie(COOKIE, tokenValido() + "manipulado")
                    .when().post(LOGOUT)
                    .then().statusCode(401);
        }
    }

    // =====================================================================================
    // Perfil autenticado
    // =====================================================================================

    @Nested
    @DisplayName("Perfil del usuario autenticado (/me)")
    class Perfil {

        @Test
        @DisplayName("La cookie del login da acceso al perfil")
        void laCookieDelLoginDaAccesoAlPerfil() {
            given().cookie(COOKIE, tokenValido()).when().get(ME)
                    .then().statusCode(200)
                    .body("username", equalTo("admin"))
                    .body("role", equalTo("SUPER_ADMIN"))
                    .body("email", equalTo("admin@viajescarolina.com"))
                    .body("passwordHash", nullValue());
        }

        @Test
        @DisplayName("Sin cookie devuelve 401")
        void sinCookieDevuelve401() {
            given().when().get(ME).then().statusCode(401);
        }

        @Test
        @DisplayName("El login actualiza la marca de último acceso")
        void elLoginActualizaElUltimoAcceso() {
            String token = tokenValido();
            String ultimoAcceso = given().cookie(COOKIE, token).when().get(ME)
                    .then().statusCode(200)
                    .extract().path("lastLoginAt");

            assertNotNull(ultimoAcceso,
                    "Tras iniciar sesión, lastLoginAt debe estar informado");
        }
    }

    // =====================================================================================
    // Utilidades
    // =====================================================================================

    private static java.net.http.HttpResponse<String> peticionCruda(
            java.net.http.HttpRequest.Builder peticion) throws Exception {
        return java.net.http.HttpClient.newHttpClient()
                .send(peticion.build(), java.net.http.HttpResponse.BodyHandlers.ofString());
    }

    private static java.util.List<String> cabecerasSetCookie(java.net.http.HttpResponse<String> r) {
        return r.headers().allValues("set-cookie");
    }

    /** Decodifica el payload del JWT sin verificar la firma (aquí solo interesan los claims). */
    private static JsonNode leerClaims(String jwt) throws Exception {
        String payload = jwt.split("\\.")[1];
        byte[] bytes = Base64.getUrlDecoder().decode(payload);
        return JSON.readTree(new String(bytes, StandardCharsets.UTF_8));
    }
}
