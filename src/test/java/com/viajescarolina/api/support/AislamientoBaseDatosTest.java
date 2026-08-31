package com.viajescarolina.api.support;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Red de seguridad del aislamiento: comprueba, EN CALIENTE, que la suite no está
 * conectada a la base de datos de desarrollo.
 *
 * <p>En {@code vc-postgres} vive el contenido real con el que el equipo valida el sitio
 * (32 promociones, el blog, las reclamaciones). Si alguien borra el bloque {@code %test}
 * de {@code application.properties}, los tests de promociones empezarían a ocultar,
 * editar y borrar ESE contenido y nadie se daría cuenta hasta abrir la web. Este test
 * falla primero y explica por qué.</p>
 */
@QuarkusTest
@DisplayName("Aislamiento — la suite corre sobre un PostgreSQL efímero, nunca sobre el de desarrollo")
class AislamientoBaseDatosTest {

    /** Nombre de la base de datos de desarrollo que la suite tiene prohibido tocar. */
    private static final String BASE_DE_DESARROLLO = "viajes_carolina_db";

    @Inject
    DataSource dataSource;

    @Test
    @DisplayName("La conexión activa NO apunta a la base de datos de desarrollo")
    void laConexionNoApuntaALaBaseDeDesarrollo() throws Exception {
        try (Connection conexion = dataSource.getConnection()) {
            String url = conexion.getMetaData().getURL();
            String baseActual = leerNombreDeBaseDeDatos(conexion);

            assertFalse(
                    BASE_DE_DESARROLLO.equals(baseActual),
                    "La suite está conectada a la base de datos de DESARROLLO (" + url + "). "
                            + "Los tests mutan promociones y bitácora: esto destruiría el contenido real. "
                            + "Revisa el bloque %test de application.properties.");

            assertTrue(
                    url.startsWith("jdbc:postgresql://"),
                    "Se esperaba un PostgreSQL efímero de Dev Services, pero la URL es: " + url);
        }
    }

    @Test
    @DisplayName("Flyway aplicó el esquema completo sobre la base efímera")
    void flywayAplicoElEsquemaCompleto() throws Exception {
        try (Connection conexion = dataSource.getConnection();
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT count(*) FROM flyway_schema_history WHERE success = true")) {
            rs.next();
            int migracionesAplicadas = rs.getInt(1);
            assertTrue(
                    migracionesAplicadas >= 62,
                    "Se esperaban al menos 62 migraciones aplicadas sobre la base efímera, hubo "
                            + migracionesAplicadas);
        }
    }

    @Test
    @DisplayName("La base efímera trae las semillas de las migraciones, no los datos de desarrollo")
    void laBaseEfimeraTraeSoloLasSemillasDeLasMigraciones() throws Exception {
        try (Connection conexion = dataSource.getConnection();
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT count(*) FROM admin_user")) {
            rs.next();
            // V14 siembra exactamente 3 usuarios (admin, editor, carolina). En desarrollo hay 5:
            // si aquí viéramos 5, estaríamos leyendo la base equivocada.
            assertEquals(3, rs.getInt(1),
                    "La base de tests debería tener solo los 3 usuarios sembrados por V14");
        }
    }

    private String leerNombreDeBaseDeDatos(Connection conexion) throws Exception {
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT current_database()")) {
            rs.next();
            return rs.getString(1);
        }
    }
}
