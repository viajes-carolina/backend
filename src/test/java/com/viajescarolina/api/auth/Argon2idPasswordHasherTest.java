package com.viajescarolina.api.auth;

import com.viajescarolina.api.auth.infrastructure.security.Argon2idPasswordHasher;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Hasheo de contraseñas administrativas. Lógica pura: se prueba sin levantar Quarkus.
 *
 * <p>Importa porque es lo único que separa la base de datos de un acceso al panel: si el
 * formato cambiara o {@code verify} dejara de leer los parámetros embebidos en el hash, los
 * usuarios sembrados por las migraciones dejarían de poder entrar.</p>
 */
@DisplayName("Argon2id — hasheo y verificación de contraseñas del panel")
class Argon2idPasswordHasherTest {

    private final Argon2idPasswordHasher hasher = new Argon2idPasswordHasher();

    @Test
    @DisplayName("Una contraseña se verifica contra su propio hash")
    void laContrasenaSeVerificaContraSuHash() {
        String hash = hasher.hash("admin123#");

        assertTrue(hasher.verify("admin123#", hash));
    }

    @ParameterizedTest(name = "\"{0}\" no debe validar contra el hash de \"admin123#\"")
    @ValueSource(strings = {"admin123", "Admin123#", "admin123# ", " admin123#", "otra-cosa"})
    @DisplayName("Cualquier variación de la contraseña es rechazada")
    void rechazaVariacionesDeLaContrasena(String intento) {
        String hash = hasher.hash("admin123#");

        assertFalse(hasher.verify(intento, hash));
    }

    @Test
    @DisplayName("El hash usa el formato PHC con los parámetros de coste esperados")
    void usaElFormatoPhcConLosParametrosEsperados() {
        String hash = hasher.hash("cualquiera");

        assertTrue(hash.startsWith("$argon2id$v=19$m=65536,t=3,p=4$"),
                "El formato del hash es contrato con las migraciones V14/V15: " + hash);
        assertEquals(6, hash.split("\\$").length);
    }

    @Test
    @DisplayName("Dos hashes de la misma contraseña son distintos (sal aleatoria)")
    void dosHashesDeLaMismaClaveSonDistintos() {
        String uno = hasher.hash("misma-clave");
        String otro = hasher.hash("misma-clave");

        assertNotEquals(uno, otro,
                "Sin sal aleatoria, dos usuarios con la misma contraseña compartirían hash");
        assertTrue(hasher.verify("misma-clave", uno));
        assertTrue(hasher.verify("misma-clave", otro));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"no-es-un-hash", "$2a$10$bcryptStyleHash", "$argon2id$roto"})
    @DisplayName("Un hash ausente o con formato inválido nunca valida")
    void hashInvalidoNuncaValida(String hashInvalido) {
        assertFalse(hasher.verify("admin123#", hashInvalido));
    }

    @Test
    @DisplayName("Lee los parámetros de coste del propio hash, no los suyos por defecto")
    void leeLosParametrosDelHashGuardado() {
        // Escenario real de rotación: un hash antiguo guardado con menos memoria e iteraciones
        // debe seguir validando, porque verify() toma m/t/p de la cadena y no de sus constantes.
        String hashAntiguo = hashConParametros("clave-antigua", 16384, 2, 1);

        assertTrue(hasher.verify("clave-antigua", hashAntiguo),
                "verify() debe respetar los parámetros embebidos en el hash almacenado");
        assertFalse(hasher.verify("clave-equivocada", hashAntiguo));
    }

    /** Construye un hash PHC con parámetros de coste distintos a los del hasher. */
    private static String hashConParametros(String clave, int memoriaKb, int iteraciones, int paralelismo) {
        byte[] sal = "sal-fija-16bytes".getBytes(StandardCharsets.UTF_8);
        Argon2Parameters parametros = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(iteraciones)
                .withMemoryAsKB(memoriaKb)
                .withParallelism(paralelismo)
                .withSalt(sal)
                .build();

        Argon2BytesGenerator generador = new Argon2BytesGenerator();
        generador.init(parametros);
        byte[] resultado = new byte[32];
        generador.generateBytes(clave.getBytes(StandardCharsets.UTF_8), resultado);

        Base64.Encoder b64 = Base64.getEncoder().withoutPadding();
        return String.format("$argon2id$v=19$m=%d,t=%d,p=%d$%s$%s",
                memoriaKb, iteraciones, paralelismo, b64.encodeToString(sal), b64.encodeToString(resultado));
    }
}
