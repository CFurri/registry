package cat.uvic.teknos.registry.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per a CryptoUtils.
 * Aquests tests depenen del fitxer 'crypto.properties' a 'src/test/resources'.
 */
class CryptoUtilsTest {

    private CryptoUtils cryptoUtils;
    private static final String EXPECTED_HASH_FOR_HELLO = "d76051e1dae76d1f309598102df58d84";
    /**
     * Aquest mètode s'assegura que existeix un fitxer crypto.properties
     * a 'build/resources/test' abans que s'executi cada test.
     * Això és vital perquè la classe CryptoUtils el pugui llegir.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Obtenim el directori on s'executen els tests (classpath)
        // Normalment és 'build/resources/test' o 'build/classes/java/test'
        String resourcesPathStr = CryptoUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        Path resourcesPath = new File(resourcesPathStr).toPath();

        // Creem el fitxer crypto.properties allà
        Path propertiesFile = resourcesPath.resolve("crypto.properties");

        Properties props = new Properties();
        props.setProperty("hashing.algorithm", "MD5");
        props.setProperty("hashing.salt", "abc");

        try (OutputStream output = Files.newOutputStream(propertiesFile)) {
            props.store(output, "Test crypto properties");
        } catch (Exception e) {
            // Si falla aquí, els tests no es poden executar
            throw new RuntimeException("No s'ha pogut crear el fitxer crypto.properties de test", e);
        }

        // Ara que el fitxer existeix, podem instanciar la classe
        cryptoUtils = new CryptoUtils();
    }

    @Test
    @DisplayName("Hash should be deterministic (same input -> same output)")
    void hashIsDeterministic() {
        String hash1 = cryptoUtils.hash("hello");
        String hash2 = cryptoUtils.hash("hello");

        assertNotNull(hash1);
        assertEquals(EXPECTED_HASH_FOR_HELLO, hash1, "El hash no coincideix amb el valor esperat.");
        assertEquals(hash1, hash2, "El hash no és determinista.");
    }

    @Test
    @DisplayName("Different inputs should produce different hashes")
    void differentInputsProduceDifferentHashes() {
        String hash1 = cryptoUtils.hash("hello");
        String hash2 = cryptoUtils.hash("world");

        assertNotEquals(hash1, hash2, "Dues entrades diferents no haurien de produir el mateix hash.");
    }

    @Test
    @DisplayName("Hash(String) should handle null input")
    void hashStringHandlesNull() {
        String hash = cryptoUtils.hash((String) null);
        assertNull(hash, "El hash d'un String null hauria de ser null.");
    }

    @Test
    @DisplayName("Hash(byte[]) should handle null input")
    void hashBytesHandlesNull() {
        String hash = cryptoUtils.hash((byte[]) null);
        assertNull(hash, "El hash d'un byte[] null hauria de ser null.");
    }

    @Test
    @DisplayName("Hash should handle empty string input")
    void hashHandlesEmptyString() {
        // 1. Calculem el hash d'un string buit
        String hash = cryptoUtils.hash("");
        assertNotNull(hash);

        // 2. Aquest és el hash MD5 esperat per al salt "abc"
        String expectedHashForEmpty = "900150983cd24fb0d6963f7d28e17f72";

        // 3. Comprovem que el hash calculat (el real) és igual a l'esperat
        assertEquals(expectedHashForEmpty, hash, "El hash d'un String buit no és correcte.");
    }
}
