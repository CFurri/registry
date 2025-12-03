package cat.uvic.teknos.registry.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    private CryptoUtils cryptoUtils;

    @BeforeEach
    void setUp() throws Exception {
        // Preparem un entorn de test (creem un crypto.properties temporal)
        String resourcesPathStr = "build/resources/test";
        File resourcesDir = new File(resourcesPathStr);
        if (!resourcesDir.exists()) resourcesDir.mkdirs();

        Path propertiesFile = resourcesDir.toPath().resolve("crypto.properties");

        Properties props = new Properties();
        props.setProperty("hashing.algorithm", "SHA-256");
        props.setProperty("hashing.salt", "SaltDeProva");
        props.setProperty("symmetric.key.size", "256");
        // No posem rutes de keystore reals per no fallar si no existeixen al test,
        // però el CryptoUtils les intentarà llegir si les cridem.

        try (OutputStream output = Files.newOutputStream(propertiesFile)) {
            props.store(output, "Test crypto properties");
        }

        // Instanciem la classe (llegirà el fitxer que acabem de crear)
        cryptoUtils = new CryptoUtils();
    }

    @Test
    @DisplayName("Hash should be deterministic (SHA-256)")
    void hashIsDeterministic() {
        String input = "Hola Món";
        String hash1 = cryptoUtils.hash(input);
        String hash2 = cryptoUtils.hash(input);

        assertNotNull(hash1);
        assertEquals(hash1, hash2, "El hash ha de ser idèntic per a la mateixa entrada");
        assertNotEquals(input, hash1, "El hash no pot ser igual al text pla");
    }

    @Test
    @DisplayName("Symmetric Encryption and Decryption should work")
    void testSymmetricEncryption() {
        // 1. Simulem un Handshake (Generem una clau de sessió i la injectem)
        SecretKey sessionKey = cryptoUtils.generateSessionKey();
        cryptoUtils.setSessionKey(sessionKey);

        String originalText = "Dades Confidencials dels Empleats";

        // 2. Xifrem
        String encryptedBase64 = cryptoUtils.encrypt(originalText);
        assertNotNull(encryptedBase64);
        assertNotEquals(originalText, encryptedBase64);

        // 3. Desxifrem
        String decryptedText = cryptoUtils.decrypt(encryptedBase64);

        // 4. Verifiquem
        assertEquals(originalText, decryptedText, "El text desxifrat ha de coincidir amb l'original");
    }
}