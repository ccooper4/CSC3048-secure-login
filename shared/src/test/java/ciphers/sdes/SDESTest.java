package ciphers.sdes;

import ciphers.BaseCipherTest;
import org.junit.Test;

public class SDESTest extends BaseCipherTest {

    public SDESTest() {
        cipher = new Cipher_SDES();
    }

    @Test
    @Override
    public void testEncryption() {
//        testEncryption("plain text", "expected result");
    }

    @Test
    @Override
    public void testDecryption() {
//        testDecryption("cipher text", "expected result");
    }
}