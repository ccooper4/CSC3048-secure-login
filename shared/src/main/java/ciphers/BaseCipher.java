package ciphers;

import static util.EncryptedLogger.info;

public abstract class BaseCipher {

    public abstract String encrypt(String plaintext);

    public abstract String decrypt(String encryptedText);

    protected void logEncryption(String input, String output) {
        info("Encrypted plaintext [" + input + "] using: " + getClass().getSimpleName());
        info("Result: " + output);
    }

    protected void logDecryption(String input, String output) {
        info("Decrypting ciphertext [" + input + "] using: " + getClass().getSimpleName());
        info("Result: " + output);
    }
}
