package cipher.ciphers.aes;

import cipher.ciphers.BaseCipherTest;
import cipher.util.AESKeyGenerator;
import org.junit.Ignore;
import org.junit.Test;
import java.util.Arrays;
import org.junit.Assert;

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
        int Nk = 4;
        KeyExpansion_AES keaes = new KeyExpansion_AES();
        Assert.assertEquals("01000000", keaes.generateRcon(4, Nk));
        Assert.assertEquals("01000000", keaes.generateRcon(5, Nk));
        Assert.assertEquals("01000000", keaes.generateRcon(6, Nk));
        Assert.assertEquals("01000000", keaes.generateRcon(7, Nk));
        
        Assert.assertEquals("02000000", keaes.generateRcon(8, Nk));
        Assert.assertEquals("02000000", keaes.generateRcon(9, Nk));
        Assert.assertEquals("02000000", keaes.generateRcon(10, Nk));
        Assert.assertEquals("02000000", keaes.generateRcon(11, Nk));
        
        Assert.assertEquals("04000000", keaes.generateRcon(12, Nk));
        Assert.assertEquals("04000000", keaes.generateRcon(13, Nk));
        Assert.assertEquals("04000000", keaes.generateRcon(14, Nk));
        Assert.assertEquals("04000000", keaes.generateRcon(15, Nk));
        
        Assert.assertEquals("08000000", keaes.generateRcon(16, Nk));
        Assert.assertEquals("08000000", keaes.generateRcon(17, Nk));
        Assert.assertEquals("08000000", keaes.generateRcon(18, Nk));
        Assert.assertEquals("08000000", keaes.generateRcon(19, Nk));
        
        Assert.assertEquals("10000000", keaes.generateRcon(20, Nk));
        Assert.assertEquals("10000000", keaes.generateRcon(21, Nk));
        Assert.assertEquals("10000000", keaes.generateRcon(22, Nk));
        Assert.assertEquals("10000000", keaes.generateRcon(23, Nk));
        
        Assert.assertEquals("20000000", keaes.generateRcon(24, Nk));
        Assert.assertEquals("20000000", keaes.generateRcon(25, Nk));
        Assert.assertEquals("20000000", keaes.generateRcon(26, Nk));
        Assert.assertEquals("20000000", keaes.generateRcon(27, Nk));
        
        Assert.assertEquals("40000000", keaes.generateRcon(28, Nk));
        Assert.assertEquals("40000000", keaes.generateRcon(29, Nk));
        Assert.assertEquals("40000000", keaes.generateRcon(30, Nk));
        Assert.assertEquals("40000000", keaes.generateRcon(31, Nk));
        
        Assert.assertEquals("80000000", keaes.generateRcon(32, Nk));
        Assert.assertEquals("80000000", keaes.generateRcon(33, Nk));
        Assert.assertEquals("80000000", keaes.generateRcon(34, Nk));
        Assert.assertEquals("80000000", keaes.generateRcon(35, Nk));
        
        Assert.assertEquals("1b000000", keaes.generateRcon(36, Nk));
        Assert.assertEquals("1b000000", keaes.generateRcon(37, Nk));
        Assert.assertEquals("1b000000", keaes.generateRcon(38, Nk));
        Assert.assertEquals("1b000000", keaes.generateRcon(39, Nk));
        
        Assert.assertEquals("36000000", keaes.generateRcon(40, Nk));
        Assert.assertEquals("36000000", keaes.generateRcon(41, Nk));
        Assert.assertEquals("36000000", keaes.generateRcon(42, Nk));
        Assert.assertEquals("36000000", keaes.generateRcon(43, Nk));
    }
}
