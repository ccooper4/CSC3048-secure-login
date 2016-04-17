package util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Wrapper class around AES cipher functionality.
 * Used specifically inside the encrypted logger for secure log output.
 */
public class AESWrapper {

    private static final Logger log = LoggerFactory.getLogger(AESWrapper.class);

    // 128 bit key
    private final String KEY = "Bar12345Bar12345";

    // The cipher type
    private final String CIPHER_INSTANCE = "AES/CBC/PKCS5PADDING";

    // 16 bytes IV
    private final String INIT_VECTOR = "RandomInitVector";

    // The encoding
    private final String ENCODING = "UTF-8";

    public AESWrapper() {

    }

    /**
     * Encrypt a piece of plain text.
     * @param plaintext The plaintext to encrypt.
     * @return          The encrypted text.
     */
    public String encrypt(String plaintext) {
        try {
            Cipher cipher = getAESCipher(Cipher.ENCRYPT_MODE);

            byte[] encrypted = cipher.doFinal(plaintext.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            log.info("Error encrypting log info", e);
        }

        return null;
    }

    /**
     * Decrypt a piece of encrypted text.
     * @param encrypted The encrypted text to decrypt.
     * @return          The decrypted text.
     */
    public String decrypt(String encrypted) {
        try {
            Cipher cipher = getAESCipher(Cipher.DECRYPT_MODE);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception e) {
            log.info("Error decrypting log info", e);
        }

        return null;
    }

    /**
     * Acquire an AES cipher instance.
     * @param mode  The mode of the cipher. (Encrypt / decrypt)
     * @return      The cipher.
     */
    private Cipher getAESCipher(int mode) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(ENCODING));
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(ENCODING), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(mode, secretKeySpec, iv);

            return cipher;
        } catch (Exception e) {
            log.info("Error decrypting log info", e);
        }
        return null;
    }

}