package cipher.ciphers.aes;

import cipher.ciphers.BaseCipher;
import cipher.util.CipherUtils;
import java.util.List;

/**
 * Cipher implementation for the AES algorithm.
 */
public class Cipher_AES extends BaseCipher {

    //region Fields

    static final String[][] DEFAULT_KEY = { {"2b", "7e", "15", "16"},
                                            {"28", "ae", "d2", "a6"},
                                            {"ab", "f7", "15", "88"},
                                            {"09", "cf", "4f", "3c"} };

    /**
     * S-Box used for sub bytes.
     * - Rijndael S-box.
     * - http://en.wikipedia.org/wiki/Rijndael_S-box
     */
    static final String S_BOX[][] = {   {"63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
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

    /**
     * S-Box used for sub bytes.
     * - Rijndael S-box.
     * - http://en.wikipedia.org/wiki/Rijndael_S-box
     */
    static final String S_BOX_REVERSE[][] = {   {"63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
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

//    | 0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
//    ---|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|--|
//            00 |52 09 6a d5 30 36 a5 38 bf 40 a3 9e 81 f3 d7 fb
//    10 |7c e3 39 82 9b 2f ff 87 34 8e 43 44 c4 de e9 cb
//    20 |54 7b 94 32 a6 c2 23 3d ee 4c 95 0b 42 fa c3 4e
//            30 |08 2e a1 66 28 d9 24 b2 76 5b a2 49 6d 8b d1 25
//            40 |72 f8 f6 64 86 68 98 16 d4 a4 5c cc 5d 65 b6 92
//            50 |6c 70 48 50 fd ed b9 da 5e 15 46 57 a7 8d 9d 84
//            60 |90 d8 ab 00 8c bc d3 0a f7 e4 58 05 b8 b3 45 06
//            70 |d0 2c 1e 8f ca 3f 0f 02 c1 af bd 03 01 13 8a 6b
//    80 |3a 91 11 41 4f 67 dc ea 97 f2 cf ce f0 b4 e6 73
//            90 |96 ac 74 22 e7 ad 35 85 e2 f9 37 e8 1c 75 df 6e
//    a0 |47 f1 1a 71 1d 29 c5 89 6f b7 62 0e aa 18 be 1b
//    b0 |fc 56 3e 4b c6 d2 79 20 9a db c0 fe 78 cd 5a f4
//    c0 |1f dd a8 33 88 07 c7 31 b1 12 10 59 27 80 ec 5f
//    d0 |60 51 7f a9 19 b5 4a 0d 2d e5 7a 9f 93 c9 9c ef
//    e0 |a0 e0 3b 4d ae 2a f5 b0 c8 eb bb 3c 83 53 99 61
//    f0 |17 2b 04 7e ba 77 d6 26 e1 69 14 63 55 21 0c 7d


    /**
     * Matrix used for mix columns.
     * - Galois Field
     */
    private final String[][] MIX_COLUMN_MATRIX = {  {"2", "3", "1", "1"},
                                                    {"1", "2", "3", "1"},
                                                    {"1", "1", "2", "3"},
                                                    {"3", "1", "1", "2"} };

    private final RoundKeyGenerator keyGen;

    private int blockSideLength = 0;

    private String[][] previousOutputState;
    private String[][] key;

    //endregion

    //region Constructors
    /**
     * Constructor - Use provided key.
     * @param key   The key to use for encryption.
     */
    public Cipher_AES(String[][] key) {
        this.key = key;
        this.keyGen = new RoundKeyGenerator(key);
    }

    /**
     * Default constructor - Use default key.
     */
    public Cipher_AES() {
        this(DEFAULT_KEY);
    }
    //endregion

    //region Getters
    public RoundKeyGenerator getKeyGenerator() {
        return keyGen;
    }

    /**
     * Getter for the previous encrypted output as a matrix.
     * @return  The matrix.
     */
    public String[][] getPreviousOutputState() {
        return this.previousOutputState;
    }

    /**
     * Getter for the key used for this AES instance.
     * @return  The key.
     */
    public String[][] getKey() {
        return this.key;
    }
    //endregion

    //region Interface Overrides
    @Override
    public String encrypt(String plaintext) {
        String[][] state = getInputBlock(plaintext);

        state = addRoundKey(state, keyGen.getFirstKey());
        state = performRounds(state);
        state = performFinalRound(state);

        previousOutputState = state;

        String output = getOutputString(state);
        logEncryption(plaintext, output);
        return output;
    }

    @Override
    public String decrypt(String encryptedText) {
        String[][] state = getInputBlock(encryptedText);

        state = performFinalRoundReverse(state);
        state = performRoundsReverse(state);
        state = addRoundKeyReverse(state, keyGen.getFirstKey());

        String output = getOutputString(state);
        logDecryption(encryptedText, output);
        return output;
    }
    //endregion

    //region Encryption Helpers
    /**
     * Perform the final processing step.
     * @param state The state to process.
     * @return      The processed state.
     */
    private String[][] performFinalRound(String[][] state) {
        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, keyGen.getLastKey());
        return state;
    }


    /**
     * Perform a designated number of rounds.
     * @param state     The initial state to perform the rounds on.
     */
    private String[][] performRounds(String[][] state) {
        for (int i = 1; i < keyGen.getNumRounds(); i++) {
            state = subBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, keyGen.getRoundKey(i));
        }

        return state;
    }

    /**
     * Perform the add round key operation on the current state.
     * @param state     The current state.
     * @param roundKey  The key to XOR with the state.
     */
    private String[][] addRoundKey(String[][] state, String[][] roundKey) {
        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {
                state[i][j] = CipherUtils.hex_XOR(state[i][j], roundKey[i][j]);
            }
        }
        return state;
    }

    /**
     * Perform the substitute bytes operation using the declared s-box.
     * @param state The state to transform.
     */
    private String[][] subBytes(String[][] state) {
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

        return state;
    }

    /**
     * Perform the shift rows operation on the current state.
     * Shift row 0 0 places, shift row 1 1 place, shift row 2 2 places and shift row 3 3 places.
     * @param state The current state.
     */
    private String[][] shiftRows(String[][] state) {
        // Invert the 2d array for row oriented processing
        String[][] invertedState = getInvertedArray(state);

        // For each row
        for (int i = 0; i < blockSideLength; i++) {
            // Shift the current row by the current index
            invertedState[i] = circularShiftRowLeft(invertedState[i], i);
        }

        // Re-invert the 2d array for further column oriented processing
        return getInvertedArray(invertedState);
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
    private String[][] mixColumns(String[][] state) {
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

        return mixedColumnState;
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
    //endregion

    //region Decryption Helpers
    private String[][] performFinalRoundReverse(String[][] state) {
        state = addRoundKeyReverse(state, keyGen.getLastKey());
        state = shiftRowsReverse(state);
        state = subBytesReverse(state);
        return state;
    }

    private String[][] performRoundsReverse(String[][] state) {
        for (int i = 1; i < keyGen.getNumRounds(); i++) {
            state = addRoundKeyReverse(state, keyGen.getRoundKey(i));
            state = mixColumnsReverse(state);
            state = shiftRowsReverse(state);
            state = subBytesReverse(state);
        }

        return state;
    }

    private String[][] addRoundKeyReverse(String[][] state, String[][] roundKey) {
        return null; // TODO
    }

    private String[][] subBytesReverse(String[][] state) {
        return null; // TODO
    }

    private String[][] shiftRowsReverse(String[][] state) {
        return null; // TODO
    }

    private String[][] mixColumnsReverse(String[][] state) {
        return null; // TODO
    }
    //endregion

    //region Shared helpers
    /**
     * Transform a hex block into a string of text.
     * @param state The hex block
     * @return      The output text
     */
    private String getOutputString(String[][] state) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {
                output.append(String.valueOf((char)Integer.parseInt((state[i][j]), 16)));
            }
        }

        return output.toString();
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

        // Account for small values, prefix zero
        for (int i = 0; i < blockSideLength; i++) {
            for (int j = 0; j < blockSideLength; j++) {
                String value = inputBlock[i][j];

                if (value.length() < 2) {
                    value = 0 + value;
                }

                inputBlock[i][j] = value;
            }
        }

        return inputBlock;
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
    //endregion

    //region Round Key Generator
    /**
     * Inner class for round key generation.
     */
    public class RoundKeyGenerator {

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

        public String[][] getFirstKey() {
            return roundKeys.get(0);
        }

        public String[][] getLastKey() {
            return roundKeys.get(roundKeys.size() -1);
        }

        public String[][] getRoundKey(int roundNumber) {
            return roundKeys.get(roundNumber);
        }

        public int getNumRounds() {
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
    //endregion
}
