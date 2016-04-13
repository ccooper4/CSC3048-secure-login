package cipher.ciphers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public abstract class BaseCipherTest {

    protected BaseCipher cipher;
    protected Logger log = LoggerFactory.getLogger(getClass());

    protected void testEncryption(String plainText, String expected) {
        assertEquals(expected, cipher.encrypt(plainText));
    }

    protected void testDecryption(String cipherText, String expected) {
        assertEquals(expected, cipher.decrypt(cipherText));
    }

    protected abstract void testEncryption();

    protected abstract void testDecryption();
}
