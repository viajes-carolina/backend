package com.viajescarolina.api.support;

import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cierra por configuración el mecanismo de aislamiento de la base de datos.
 *
 * <p>{@code AislamientoBaseDatosTest} comprueba el resultado (la conexión activa no es la de
 * desarrollo) pero solo puede hacerlo una vez el contenedor ya arrancó. Este test lee
 * directamente {@code application.properties} y verifica la causa: que el perfil {@code test} no
 * declara ninguna URL JDBC —la ausencia es lo que dispara Dev Services— y que {@code dev} y
 * {@code prod} sí apuntan a PostgreSQL. Es una prueba de configuración pura, sin Quarkus ni
 * Docker, así que también protege el despliegue: si alguien rompiera la expresión del perfil
 * {@code prod}, se vería aquí y no en producción.</p>
 */
@DisplayName("Configuración — la base de datos se declara por perfil (aislamiento de la suite)")
class ConfiguracionPorPerfilTest {

    private static final String URL_JDBC = "quarkus.datasource.jdbc.url";

    private static SmallRyeConfig configuracionDelPerfil(String perfil) throws Exception {
        URL propiedades = ConfiguracionPorPerfilTest.class.getResource("/application.properties");
        assertNotNull(propiedades, "No se encontró application.properties en el classpath");
        return new SmallRyeConfigBuilder()
                // Solo el fichero: sin variables de entorno ni propiedades del sistema, para que
                // el resultado no dependa de la máquina donde corra la suite.
                .withSources(new PropertiesConfigSource(propiedades, 250))
                .withProfile(perfil)
                .build();
    }

    @Test
    @DisplayName("El perfil test NO declara URL JDBC: esa ausencia es lo que activa Dev Services")
    void elPerfilTestNoDeclaraUrlJdbc() throws Exception {
        Optional<String> url = configuracionDelPerfil("test").getOptionalValue(URL_JDBC, String.class);

        assertTrue(url.isEmpty(), """
                El perfil test declara una URL JDBC (%s). Con una URL explícita, Quarkus NO levanta \
                el contenedor efímero y la suite escribiría sobre la base de desarrollo, donde vive \
                el contenido real del sitio. Declara la conexión solo bajo %%dev y %%prod.\
                """.formatted(url.orElse("")));
    }

    @ParameterizedTest(name = "El perfil {0} sí apunta a la base de la aplicación")
    @ValueSource(strings = {"dev", "prod"})
    @DisplayName("Los perfiles dev y prod siguen conectando a PostgreSQL")
    void losPerfilesRealesConectanAPostgres(String perfil) throws Exception {
        String url = configuracionDelPerfil(perfil).getValue(URL_JDBC, String.class);

        assertEquals("jdbc:postgresql://localhost:5432/viajes_carolina_db", url,
                "La expresión del perfil " + perfil + " debe resolver a la base de la aplicación");
    }

    @ParameterizedTest(name = "El perfil {0} conserva usuario y contraseña")
    @ValueSource(strings = {"dev", "prod"})
    @DisplayName("Los perfiles dev y prod conservan las credenciales de la base")
    void losPerfilesRealesConservanCredenciales(String perfil) throws Exception {
        SmallRyeConfig config = configuracionDelPerfil(perfil);

        assertEquals("postgres", config.getValue("quarkus.datasource.username", String.class));
        assertEquals("postgres", config.getValue("quarkus.datasource.password", String.class));
    }

    @Test
    @DisplayName("En tests la publicación en Facebook queda desactivada")
    void enTestsNoSePublicaEnFacebook() throws Exception {
        SmallRyeConfig config = configuracionDelPerfil("test");

        assertEquals(false, config.getValue("viajescarolina.facebook.publish-enabled", Boolean.class),
                "Crear una promoción en un test no puede publicar nada en la Página real");
        assertTrue(config.getOptionalValue("viajescarolina.facebook.page-access-token", String.class).isEmpty(),
                "El token de la Página no debe estar disponible durante los tests");
    }

    @Test
    @DisplayName("En tests la revalidación ISR no apunta al Next.js de desarrollo")
    void enTestsNoSeRevalidaElFrontendDeDesarrollo() throws Exception {
        String url = configuracionDelPerfil("test")
                .getValue("viajescarolina.publishing.revalidation-url", String.class);

        assertTrue(!url.contains(":3000"),
                "Un test no debe poder disparar una revalidación contra el frontend local: " + url);
    }

    @Test
    @DisplayName("La cookie de sesión solo pierde el flag Secure en desarrollo")
    void laCookieSoloEsInseguraEnDesarrollo() throws Exception {
        String propiedad = "viajescarolina.security.cookie-secure";

        assertEquals(false, configuracionDelPerfil("dev").getValue(propiedad, Boolean.class),
                "En local el panel va por HTTP: ahí la cookie no puede exigir Secure");
        assertEquals(true, configuracionDelPerfil("prod").getValue(propiedad, Boolean.class),
                "Fuera de desarrollo la cookie de sesión debe viajar solo por HTTPS");
        assertEquals(true, configuracionDelPerfil("test").getValue(propiedad, Boolean.class),
                "La suite verifica el endurecimiento por defecto, no la relajación de desarrollo");
    }
}
