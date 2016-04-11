package ciphers;

import util.EncryptedLogger;

public abstract class BaseCipher {
    
    public EncryptedLogger log = new EncryptedLogger(getClass());

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
