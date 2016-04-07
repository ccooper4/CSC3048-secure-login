package ciphers.aes;

import ciphers.BaseCipher;
import ciphers.CipherUtils;
import java.util.List;

public class Cipher_AES extends BaseCipher {

    private static final String[][] DEFAULT_KEY = { {"2b", "7e", "15", "16"},
                                                    {"28", "ae", "d2", "a6"},
                                                    {"ab", "f7", "15", "88"},
                                                    {"09", "cf", "47", "3c"} };

    // Matrix used for mix columns
    private final String[][] MIX_COLUMN_MATRIX = {  {"2", "3", "1", "1"},
                                                    {"1", "2", "3", "1"},
                                                    {"1", "1", "2", "3"},
                                                    {"3", "1", "1", "2"} };

    private final String[][] MIX_COLUMN_TEST = {    {"d4", "bf", "5d", "30"},
                                                    {"e0", "b4", "52", "ae"},
                                                    {"b8", "41", "11", "f1"},
                                                    {"1e", "27", "98", "e5"} };

    private final String[][] ROUND_KEY_TEST_KEY = { {"a0", "fa", "fe", "17"},
                                                    {"88", "54", "2c", "b1"},
                                                    {"23", "a3", "39", "39"},
                                                    {"2a", "6c", "76", "05"} };

    private final String[][] ROUND_KEY_TEST_STATE = {   {"04", "66", "81", "e5"},
                                                        {"e0", "cb", "19", "9a"},
                                                        {"48", "f8", "d3", "26"},
                                                        {"e5", "9a", "7a", "4c"} };


    // S-Box used for sub bytes
    public static final String S_BOX[][] = {  {"63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
                                        {"ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af", "9c", "a4", "72", "c0"},
                                        {"b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1", "71", "d8", "31", "15"},
                                        {"04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2", "eb", "27", "b2", "75"},
                                        {"09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3", "29", "e3", "2f", "84"},
                                        {"53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39", "4a", "4c", "58", "cf"},
                                        {"d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f", "50", "3c", "9f", "a8"},
                                        {"51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21", "10", "ff", "f3", "d2"},
                                        {"cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d", "64", "5d", "19", "73"},
                                        {"60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14", "de", "5e", "0b", "db"},
                                        {"e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62", "91", "95", "e4", "79"},
                                        {"e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea", "65", "7a", "ae", "08"},
                                        {"ba", "78", "25", "2e", "1c", "a6", "b4", "c6", "e8", "dd", "74", "1f", "4b", "bd", "8b", "8a"},
                                        {"70", "3e", "b5", "66", "48", "03", "f6", "0e", "61", "35", "57", "b9", "86", "c1", "1d", "9e"},
                                        {"e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9", "ce", "55", "28", "df"},
                                        {"8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f", "b0", "54", "bb", "16"} };

    private final RoundKeyGenerator keyGen;

    private int blockSideLength = 0;

    /**
     * Constructor - Use provided key.
     * @param key   The key to use for encryption.
     */
    public Cipher_AES(String[][] key) {
        this.keyGen = new RoundKeyGenerator(key);
    }

    /**
     * Default constructor - Use default key.
     */
    public Cipher_AES() {
        this(DEFAULT_KEY);
    }

    @Override
    public String encrypt(String plaintext) {
        String[][] state = getInputBlock(plaintext);

        addRoundKey(state, keyGen.getFirstKey());
        performRounds(state);
        performFinalRound(state);

        String output = getOutputString(state);
        logEncryption(plaintext, output);
        return output;
    }

    @Override
    public String decrypt(String encryptedText) {
        String output = null;
        logDecryption(encryptedText, output);
        return null;
    }

    /**
     * Perform the final processing step.
     * @param state The state to process.
     * @return      The processed state.
     */
    private String[][] performFinalRound(String[][] state) {
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, keyGen.getLastKey());
        return state;
    }

    /**
     * Perform a designated number of rounds.
     * @param state     The initial state to perform the rounds on.
     */
    private void performRounds(String[][] state) {
        for (int i = 1; i < keyGen.getNumRounds(); i++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, keyGen.getRoundKey(i));
        }
    }

    /**
     * Transform a string of plaintext into a hex block
     * @param input The input text
     * @return      The hex block
     */
    private String[][] getInputBlock(String input) {
        int charCount = input.length();
        blockSideLength = charCount / 4;

        String[][] inputBlock = new String[blockSideLength][blockSideLength];

        int charIndex = 0;
        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {
                inputBlock[i][j] = Integer.toHexString((int) input.charAt(charIndex));
                charIndex ++;
            }
        }

        return inputBlock;
    }

    /**
     * Transform a hex block into a string of text.
     * @param state The hex block
     * @return      The output text
     */
    private String getOutputString(String[][] state) {
        return null;
    }

    /**
     * Perform the add round key operation on the current state.
     * @param state     The current state.
     * @param roundKey  The key to XOR with the state.
     */
    private void addRoundKey(String[][] state, String[][] roundKey) {
        roundKey = ROUND_KEY_TEST_KEY;
        state = ROUND_KEY_TEST_STATE;
        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {
                state[i][j] = CipherUtils.hex_XOR(state[i][j], roundKey[i][j]);
            }
        }
    }

    /**
     * Perform the substitute bytes operation using the declared s-box.
     * @param state The state to transform.
     */
    private void subBytes(String[][] state) {
        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {

                // Get the left and right piece
                String left = state[i][j].substring(0, 1);
                String right = state[i][j].substring(1, 2);

                // Get the row and column in the array
                int row = CipherUtils.hexToDecimal(left);
                int column = CipherUtils.hexToDecimal(right);

                // Get new value and overwrite old value in state
                String lookup = S_BOX[row][column];
                state[i][j] = lookup;
            }
        }
    }

    /**
     * Perform the shift rows operation on the current state.
     * Shift row 0 0 places, shift row 1 1 place, shift row 2 2 places and shift row 3 3 places.
     * @param state The current state.
     */
    private void shiftRows(String[][] state) {
        // Invert the 2d array for row oriented processing
        String[][] invertedState = getInvertedArray(state);

        // For each row
        for (int i = 0; i < blockSideLength; i++) {
            // Shift the current row by the current index
            invertedState[i] = circularShiftRowLeft(invertedState[i], i);
        }

        // Re-invert the 2d array for further column oriented processing
        state = getInvertedArray(invertedState);
    }

    /**
     * Perform a shift row operation, to the left.
     * @param array The array to shift.
     * @param shift The amount of places to shift.
     */
    private String[] circularShiftRowLeft(String[] array, int shift) {
        String[] temp = new String[array.length];

        // Copy non shifted section to start of new array
        System.arraycopy(array, shift, temp, 0, array.length - shift);
        // Copy shifted section to end of new array
        System.arraycopy(array, 0, temp, array.length - shift, shift);

        return temp;
    }

    /**
     * Perform a mix columns operation on the current state.
     * @param state The state.
     */
    private void mixColumns(String[][] state) {
        state = MIX_COLUMN_TEST;

        // Invert the state to get columns
        String[][] mixedColumnState = new String[blockSideLength][blockSideLength];

        // For each column in the column state
        for (int i = 0; i < blockSideLength; i++) {
            String[] binaryColumn = new String[blockSideLength];

            // Convert current column to binary
            for (int j = 0; j < blockSideLength; j++) {
                binaryColumn[j] = CipherUtils.hexToBinaryString(state[i][j]);
            }

            String[] resultColumn = new String[blockSideLength];

            // For each entry in the column
            for (int j = 0; j < blockSideLength; j++) {

                // Get the matrix row for this entry
                String[] matrixRow = MIX_COLUMN_MATRIX[j];

                // Perform the mix column operation
                resultColumn[j] = performMixColumn(binaryColumn, matrixRow);
            }

            // Convert result column to hex and store
            for (int j = 0; j < blockSideLength; j++) {
                mixedColumnState[i][j] = CipherUtils.binaryStringToHex(resultColumn[j]);
            }
        }

        state = mixedColumnState;
    }

    /**
     * Perform the mix column operation.
     * @param column        The column of binary strings.
     * @param matrixRow     The row from the galois matrix.
     * @return              The result of the operation.
     */
    private String performMixColumn(String[] column, String[] matrixRow) {
        String[] results = new String[blockSideLength];

        // Populate results
        for (int i = 0; i < blockSideLength; i++) {
            String binaryValue = column[i];
            String matrixValue = matrixRow[i];

            switch (matrixValue) {
                case "1":   results[i] = binaryValue;
                            break;
                case "2":   results[i] = perform02(binaryValue);
                            break;
                case "3":   results[i] = perform03(binaryValue);
            }
        }

        // Perform XOR on results
        String result1 = CipherUtils.binary_XOR(results[0], results[1]);
        String result2 = CipherUtils.binary_XOR(result1, results[2]);
        String result3 = CipherUtils.binary_XOR(result2, results[3]);

        return result3;
    }

    /**
     * Invert the columns and rows of a 2d array.
     * @param array The array to invert.
     * @return      The inverted array.
     */
    private String[][] getInvertedArray(String[][] array) {
        String[][] invertedArray = new String[blockSideLength][blockSideLength];

        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {
                invertedArray[i][j] = array[j][i];
            }
        }

        return invertedArray;
    }

    /**
     * Perform the 03 operation.
     * @param binary    The binary string.
     * @return          The result of 03.
     */
    private String perform03(String binary) {
        return CipherUtils.hex_XOR(binary, perform02(binary));
    }

    /**
     * Perform the 02 operation.
     * If left most bit is 0, shift left only.
     * If left most bit is 1, shift left and XOR with 27
     * @param binary    The binary string.
     * @return          The result of 02.
     */
    private String perform02(String binary) {
        final String binary27 = CipherUtils.decimalToBinaryString(27);
        String result;

        if (binary.substring(0,1).equals("0")) {
            result = CipherUtils.shiftBinaryStringLeft(binary, 1);
        } else {
            String shifted = CipherUtils.shiftBinaryStringLeft(binary, 1);
            result = CipherUtils.binary_XOR(shifted, binary27);
        }

        return result;
    }

    /**
     * Inner class for round key generation.
     */
    private class RoundKeyGenerator {

        private int numRounds;
        private List<String[][]> roundKeys; // 4x4 arrays in list - 11 of these
        private String[][] initialKey;

        /**
         * Constructor.
         * @param key   The key to generate round keys from.
         */
        private RoundKeyGenerator(String[][] key) {
            this.initialKey = key;
            roundKeys = performKeyExpansion();
        }

        private String[][] getFirstKey() {
            return roundKeys.get(0);
        }

        private String[][] getLastKey() {
            return roundKeys.get(roundKeys.size() -1);
        }

        private String[][] getRoundKey(int roundNumber) {
            return roundKeys.get(roundNumber);
        }

        private int getNumRounds() {
            return numRounds;
        }

        /**
         * Calculate the number of rounds to perform based on the set encryption key.
         */
        private int calculateNumRounds() {
            return 10; // TODO: Use key to calculate round count
        }

        /**
         * Generate a list of round keys based on the initial key.
         * @return  The list of round keys.
         */
        private List<String[][]> performKeyExpansion() {
            numRounds = calculateNumRounds();
            KeyExpansion_AES keyExpansion_aes = new KeyExpansion_AES();
            return keyExpansion_aes.keyExpansion(initialKey, 4, numRounds, 4);
        }
    }
}
