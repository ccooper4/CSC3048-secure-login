package qub.util;

import static util.RandomNumberGen.getRandom;

/**
 * Generates a new random identifier.
 */
public class LoginIdGenerator {

    private static final int START_INDEX = 10000000;
    private static final int END_INDEX =   99999999;

    /**
     * Generate a new ID.
     * @return  The ID.
     */
    public static String newId() {
        return String.valueOf(getRandom(START_INDEX, END_INDEX));
    }
}
