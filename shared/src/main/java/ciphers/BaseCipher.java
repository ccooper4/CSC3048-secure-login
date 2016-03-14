package ciphers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCipher {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract String encrypt(String plaintext);

    public abstract String decrypt(String encryptedText);

    protected void logEncryption(String input, String output) {
        log.info("Encrypted plaintext [" + input + "] using: " + getClass().getSimpleName());
        log.info("Result: " + output);
    }

    protected void logDecryption(String input, String output) {
        log.info("Decrypting ciphertext [" + input + "] using: " + getClass().getSimpleName());
        log.info("Result: " + output);
    }
}
