package cipher.util;

import java.util.Random;

/**
 *  Utility class for generating a random key to be used for AES encryption
 */
public class AESKeyGenerator {

    private static final int DEFAULT_KEY_SIZE = 16;
    private static final String[] DATASET = {  "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                                "a", "b", "c", "d", "e", "f"};
    private static Random random = new Random();

    /**
     * Generate a new AES key using the default size of 128 bits / 16 bytes.
     * @return  The generated key.
     */
    public static String[][] generateAESKey() {
        return generateAESKey(DEFAULT_KEY_SIZE);
    }

    /**
     * Generate a new AES key using the given key size.
     * @param keySizeBytes  The key size to generate.
     * @return              The generated key.
     */

    private static String[][] generateAESKey(int keySizeBytes) {
        int blockSideLength = keySizeBytes / 4;
        String[][] key = new String[blockSideLength][blockSideLength];

        // column index
        for (int i = 0; i < blockSideLength; i++) {
            // entries index
            for (int j = 0; j < blockSideLength; j++) {

                int leftIndex = getRandom(0, DATASET.length - 1);
                int rightIndex = getRandom(0, DATASET.length - 1);

                String leftEntry = DATASET[leftIndex];
                String rightEntry = DATASET[rightIndex];

                key[i][j] = leftEntry + rightEntry;
            }
        }

        return key;
    }

    /**
     * Generate a random number between the bounds set (inclusive).
     * @param min   The lower bound.
     * @param max   The upper bound.
     * @return      The random number.
     */
    private static int getRandom(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

}
