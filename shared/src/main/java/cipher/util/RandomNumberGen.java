package cipher.util;

import java.util.Random;

public class RandomNumberGen {

    private static Random random = new Random();

    /**
     * Generate a random number between the bounds set (inclusive).
     * @param min   The lower bound.
     * @param max   The upper bound.
     * @return      The random number.
     */
    public static int getRandom(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
