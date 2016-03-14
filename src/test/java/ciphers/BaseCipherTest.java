package ciphers;

import static org.junit.Assert.assertEquals;

public abstract class BaseCipherTest {

    protected BaseCipher cipher;

    protected void testEncryption(String plainText, String expected) {
        assertEquals(expected, cipher.encrypt(plainText));
    }

    protected void testDecryption(String cipherText, String expected) {
        assertEquals(expected, cipher.decrypt(cipherText));
    }

    protected abstract void testEncryption();

    protected abstract void testDecryption();
}
