package cipher.ciphers.aes;

import cipher.ciphers.BaseCipherTest;
import cipher.util.AESKeyGenerator;
import org.junit.Ignore;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class AESTest extends BaseCipherTest {

    public AESTest() {
        cipher = new Cipher_AES();
    }

    @Test
    @Override
    public void testEncryption() {
        // Examination key
        printMatrix(((Cipher_AES) cipher).getKey(), "Key used for encryption:");

        // Examination test case
        testEncryption("mark frequency  ", "@8{³&\u0000³Z²8\fF\u001FÀÃr");
        printMatrix(((Cipher_AES) cipher).getPreviousOutputState(), "Output in raw state:");

        // Test case from notes
        testEncryption("2Cö¨\u0088Z0\u008D11\u0098¢à7\u00074", "9%\u0084\u001D\u0002Ü\tûÜ\u0011\u0085\u0097\u0019j\u000B2");
        printMatrix(((Cipher_AES) cipher).getPreviousOutputState(), "Output in raw state:");
    }

    @Test
    @Override
    public void testDecryption() {
        testDecryption("@8{³&\u0000³Z²8\fF\u001FÀÃr", "mark frequency  ");
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

    @Test
    @Ignore
    public void testEncryptionWithRandomKey() {
        // Get new random key
        String[][] key = AESKeyGenerator.generateAESKey();
        printMatrix(key);

        // Create a cipher with this key
        cipher = new Cipher_AES(key);

        String cipherText = cipher.encrypt("mark frequency  ");
        assertNotNull(cipherText);
    }

    /**
     * Print out a 2D array, for marking purposes.
     * @param matrix    The matrix to print.
     * @param messages  Optional output messages
     */
    private void printMatrix(String[][] matrix, String... messages) {
        for (String message : messages) {
            log.info(message);
        }

        log.info(Arrays.deepToString(matrix) + "\n");
    }
    @Test
    public void testRconGeneration() {
//        KeyExpansion_AES keaes = new KeyExpansion_AES();
//        keaes.generateRcon(1, 4, 1);
//        keaes.generateRcon(2, 4, 2);
//        keaes.generateRcon(3, 4, 3);
//        keaes.generateRcon(4, 4, 4);
//        keaes.generateRcon(5, 4, 5);
//        keaes.generateRcon(6, 4, 6);
//        keaes.generateRcon(7, 4, 7);
//        keaes.generateRcon(8, 4, 8);
//        keaes.generateRcon(9, 4, 9);
//        keaes.generateRcon(10, 4, 10);
    }
}
