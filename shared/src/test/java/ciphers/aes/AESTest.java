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
        testEncryption("mark frequency  ", "@8{³&\u0000³Z²8\fF\u001FÀÃr");
        testEncryption("2Cö¨\u0088Z0\u008D11\u0098¢à7\u00074", "9%\u0084\u001D\u0002Ü\tûÜ\u0011\u0085\u0097\u0019j\u000B2");
    }

    @Test
    @Override
    public void testDecryption() {
//        testDecryption("@8{³&\u0000³Z²8\fF\u001FÀÃr", "mark frequency  ");
    }
    
    @Test
    public void testKeyExpansion() {
        String[][] actualInitialRound = ((Cipher_AES) cipher).getKeyGenerator().getFirstKey();
        String[][] actualFirstRound = ((Cipher_AES) cipher).getKeyGenerator().getRoundKey(1);
        String[][] actualFinalRound = ((Cipher_AES) cipher).getKeyGenerator().getLastKey();

        String[][] expectedInitialRound = Cipher_AES.DEFAULT_KEY;

        String[][] expectedFirstRound = {   {"a0", "fa", "fe", "17"},
                                            {"88", "54", "2c", "b1"},
                                            {"23", "a3", "39", "39"},
                                            {"2a", "6c", "76", "05"} };

        String[][] expectedFinalRound = {   {"d0", "14", "f9", "a8"},
                                            {"c9", "ee", "25", "89"},
                                            {"e1", "3f", "0c", "c8"},
                                            {"b6", "63", "0c", "a6"} };

        assertArrayEquals(expectedInitialRound, actualInitialRound);
        assertArrayEquals(expectedFirstRound, actualFirstRound);
        assertArrayEquals(expectedFinalRound, actualFinalRound);
    }
}