package ciphers.aes;

import ciphers.BaseCipherTest;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class AESTest extends BaseCipherTest {

    public AESTest() {
        cipher = new Cipher_AES();
    }

    @Test
    @Override
    public void testEncryption() {
        // TODO: Add expected result.
//        testEncryption("mark frequency  ", "expected result");
    }

    @Test
    @Override
    public void testDecryption() {
//        testDecryption("cipher text", "expected result");
    }
    
    @Test
    public void testKeyExpansion() {
        String[][] actualInitialRound = ((Cipher_AES) cipher).getKeyGenerator().getFirstKey();
        String[][] actualFinalRound = ((Cipher_AES) cipher).getKeyGenerator().getLastKey();

        // TODO: Add expected values.
        String[][] expectedInitialRound = null;
        String[][] expectedFinalRound = null;

        assertArrayEquals(expectedInitialRound, actualInitialRound);
        assertArrayEquals(expectedFinalRound, actualFinalRound);
    }
}