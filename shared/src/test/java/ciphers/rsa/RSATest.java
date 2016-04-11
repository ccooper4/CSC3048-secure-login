package ciphers.rsa;

import ciphers.BaseCipherTest;
import org.junit.Test;

public class RSATest extends BaseCipherTest {

    public RSATest() {
        cipher = new Cipher_RSA();
    }

    @Test
    @Override
    public void testEncryption() {
//        testEncryption("ZZ", "expected result");
    }

    @Test
    @Override
    public void testDecryption() {
//        testDecryption("cipher text", "expected result");
    }
}