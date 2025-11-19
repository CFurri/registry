package cat.uvic.teknos.registry.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class CryptoUtils {

    private final String algorithm;
    private final String salt;
    private static final String PROPERTIES_FILE = "/crypto.properties";

    /**
     * Constructor que carrega la configuració des del fitxer crypto.properties.
     */
    public CryptoUtils() {
        Properties properties = new Properties();
        try (InputStream input = CryptoUtils.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("No s'ha pogut trobar el fitxer de propietats: " + PROPERTIES_FILE);
            }
            properties.load(input);
            this.algorithm = properties.getProperty("hashing.algorithm"); //
            this.salt = properties.getProperty("hashing.salt"); //
        } catch (IOException e) {
            throw new RuntimeException("No s'ha pogut carregar el fitxer de propietats", e);
        }
    }

    /**
     * Calcula el hash d'un text pla (String).
     *
     * @param plainText El text a "hashejar".
     * @return El hash resultant en format hexadecimal.
     */
    public String hash(String plainText) {
        if (plainText == null) {
            return null;
        }
        // Converteix el text a bytes usant UTF-8 i crida el mètode principal
        return hash(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Calcula el hash d'un array de bytes.
     *
     * @param plainText Els bytes a "hashejar".
     * @return El hash resultant en format hexadecimal.
     */
    public String hash(byte[] plainText) {
        if (plainText == null) {
            return null;
        }

        try {
            // Obté la instància de l'algorisme (MD5)
            MessageDigest md = MessageDigest.getInstance(this.algorithm);

            // 1. Aplica el salt
            md.update(this.salt.getBytes(StandardCharsets.UTF_8));

            // 2. Afegeix el text pla i calcula el resum final
            byte[] digest = md.digest(plainText);

            // 3. Converteix el resultat de bytes a hexadecimal
            return toHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            // MD5 hauria d'existir sempre, si no, és un error greu de l'entorn Java
            throw new RuntimeException("Algorisme de hash no trobat: " + this.algorithm, e);
        }
    }

    /**
     * Mètode privat per convertir un array de bytes a la seva representació
     * hexadecimal (String).
     *
     * @param hash L'array de bytes del resum.
     * @return Un String hexadecimal.
     */
    private String toHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}