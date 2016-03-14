package ciphers.sdes;

import ciphers.BaseCipher;

public class Cipher_SDES extends BaseCipher {

    @Override
    public String encrypt(String plaintext) {
        String output = null;

        logEncryption(plaintext, output);
        return null;
    }

    @Override
    public String decrypt(String encryptedText) {
        String output = null;

        logDecryption(encryptedText, output);
        return null;
    }
}
