package ciphers.aes;

import ciphers.BaseCipherTest;
import org.junit.Test;

public class AESTest extends BaseCipherTest {

    public AESTest() {
        cipher = new Cipher_AES();
    }

    @Test
    @Override
    public void testEncryption() {
//        testEncryption("mark frequency  ", "expected result");
    }

    @Test
    @Override
    public void testDecryption() {
//        testDecryption("cipher text", "expected result");
    }
}