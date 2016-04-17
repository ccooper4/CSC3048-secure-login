package cipher.ciphers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCipher {
    
    public Logger log = LoggerFactory.getLogger(getClass());

    public abstract String encrypt(String plaintext);

    public abstract String decrypt(String encryptedText);
    
    protected void logMessage(String input) {
        log.info(input);
    }
    
    protected void log(String input, String output, String conversion) {
        log.info(conversion + " [" + input + "] using: " + getClass().getSimpleName());
        log.info("Result: " + output);
    }

    protected void logEncryption(String input, String output) {
        log.info("Encrypted plaintext [" + input + "] using: " + getClass().getSimpleName());
        log.info("Result: " + output);
    }

    protected void logDecryption(String input, String output) {
        log.info("Decrypting ciphertext [" + input + "] using: " + getClass().getSimpleName());
        log.info("Result: " + output);
    }
}
