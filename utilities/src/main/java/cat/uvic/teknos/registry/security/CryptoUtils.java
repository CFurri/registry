package cat.uvic.teknos.registry.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

//P4
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Properties;

public class CryptoUtils {
    // Propietats
    private final String hashAlgorithm;
    private final String salt;
    private final String symmetricTransformation;
    private final int symmetricKeySize;
    private final String keystorePassword;

    // Clau de sessió (Simètrica) - Aquesta canviarà dinàmicament!
    private SecretKey sessionKey;

    private static final String PROPERTIES_FILE = "/crypto.properties";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    public CryptoUtils() {
        Properties props = new Properties();
        try (InputStream input = CryptoUtils.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) throw new RuntimeException("Fitxer no trobat: " + PROPERTIES_FILE);
            props.load(input);

            this.hashAlgorithm = props.getProperty("hashing.algorithm");
            this.salt = props.getProperty("hashing.salt");
            this.symmetricTransformation = props.getProperty("symmetric.transformation", "AES/GCM/NoPadding");
            this.symmetricKeySize = Integer.parseInt(props.getProperty("symmetric.key.size", "256"));
            this.keystorePassword = props.getProperty("keystore.password");

        } catch (Exception e) {
            throw new RuntimeException("Error carregant crypto.properties", e);
        }
    }

    // --- 1. HASHING ---

    public String hash(String plainText) {
        if (plainText == null) return null;
        return hash(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public String hash(byte[] bytes) {
        if (bytes == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest(bytes);
            return Base64.getEncoder().encodeToString(digest); // Base64 és més estàndard que Hex manual
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error en hash", e);
        }
    }

    // --- 2. SYMMETRIC ENCRYPTION (AES) ---

    // Permet establir la clau de sessió manualment (pel Handshake)
    public void setSessionKey(SecretKey key) {
        this.sessionKey = key;
    }

    public SecretKey getSessionKey() {
        return this.sessionKey;
    }

    public SecretKey generateSessionKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(symmetricKeySize);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generant clau AES", e);
        }
    }

    public String encrypt(String plainText) {
        if (this.sessionKey == null) throw new IllegalStateException("Session Key no establerta!");
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(symmetricTransformation);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey, spec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Format: IV + CipherText
            byte[] encryptedMessage = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedMessage, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedMessage, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(encryptedMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error xifrant simètricament", e);
        }
    }

    public String decrypt(String base64CipherText) {
        if (this.sessionKey == null) throw new IllegalStateException("Session Key no establerta!");
        try {
            byte[] decodedMessage = Base64.getDecoder().decode(base64CipherText);

            // Separar IV i CipherText
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(decodedMessage, 0, iv, 0, iv.length);

            int cipherTextSize = decodedMessage.length - GCM_IV_LENGTH;
            byte[] cipherText = new byte[cipherTextSize];
            System.arraycopy(decodedMessage, iv.length, cipherText, 0, cipherTextSize);

            Cipher cipher = Cipher.getInstance(symmetricTransformation);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, sessionKey, spec);

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error desxifrant simètricament", e);
        }
    }

    // --- 3. ASYMMETRIC ENCRYPTION (RSA) ---

    private KeyStore loadKeyStore(String path) {
        try (InputStream is = CryptoUtils.class.getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("KeyStore no trobat: " + path);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(is, keystorePassword.toCharArray());
            return ks;
        } catch (Exception e) {
            throw new RuntimeException("Error carregant KeyStore: " + path, e);
        }
    }

    // Xifra amb la clau PÚBLICA del destinatari (extreta del certificat al KeyStore)
    public String asymmetricEncrypt(String plainText, String keyStorePath, String alias) {
        try {
            KeyStore ks = loadKeyStore(keyStorePath);
            Certificate cert = ks.getCertificate(alias);
            if (cert == null) throw new RuntimeException("Alias no trobat: " + alias);
            PublicKey publicKey = cert.getPublicKey();

            Cipher cipher = Cipher.getInstance("RSA"); // O "RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error xifrant asimètricament", e);
        }
    }

    // Desxifra amb la clau PRIVADA pròpia (del KeyStore)
    public String asymmetricDecrypt(String base64CipherText, String keyStorePath, String alias) {
        try {
            KeyStore ks = loadKeyStore(keyStorePath);
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keystorePassword.toCharArray());
            if (privateKey == null) throw new RuntimeException("Clau privada no trobada per alias: " + alias);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error desxifrant asimètricament", e);
        }
    }
}
