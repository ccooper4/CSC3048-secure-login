package ciphers.aes;

import ciphers.BaseCipher;

import java.util.ArrayList;
import java.util.List;

public class Cipher_AES extends BaseCipher {

    private static String[] DEFAULT_KEY = { "2b", "28", "ab", "09",
                                            "7e", "ae", "f7", "cf",
                                            "15", "d2", "15", "4f",
                                            "16", "a6", "88", "3c" };
    private final RoundKeyGenerator keyGen;

    /**
     * Constructor - Use provided key.
     * @param key   The key to use for encryption.
     */
    public Cipher_AES(String[] key) {
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
        String[] state = getInputBlock(plaintext);

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
    private String[] performFinalRound(String[] state) {
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, keyGen.getLastKey());
        return state;
    }

    /**
     * Perform a designated number of rounds.
     * @param state     The initial state to perform the rounds on.
     */
    private void performRounds(String[] state) {
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
    private String[] getInputBlock(String input) {
        int blockSize = 16;
        String[] inputBlock = new String[blockSize];

        for (int i = 0; i < blockSize; i++) {
            inputBlock[i] = Integer.toHexString((int) input.charAt(i));
        }

        return inputBlock;
    }

    /**
     * Transform a hex block into a string of text.
     * @param state The hex block
     * @return      The output text
     */
    private String getOutputString(String[] state) {
        return null;
    }

    /**
     *
     * @param state
     * @param roundKey
     */
    private void addRoundKey(String[] state, String[] roundKey) {
    }

    /**
     *
     * @param state
     */
    private void subBytes(String[] state) {

    }

    /**
     *
     * @param state
     */
    private void shiftRows(String[] state) {

    }

    /**
     *
     * @param state
     */
    private void mixColumns(String[] state) {

    }

    /**
     * Inner class for round key generation.
     */
    private class RoundKeyGenerator {

        private int numRounds;
        private List<String[]> roundKeys;
        private String[] initialKey;

        /**
         * Constructor.
         * @param key   The key to generate round keys from.
         */
        private RoundKeyGenerator(String[] key) {
            this.initialKey = key;
            roundKeys = performKeyExpansion();
        }

        private String[] getFirstKey() {
            return roundKeys.get(0);
        }

        private String[] getLastKey() {
            return roundKeys.get(roundKeys.size() -1);
        }

        private String[] getRoundKey(int roundNumber) {
            return roundKeys.get(roundNumber);
        }

        private int getNumRounds() {
            return numRounds;
        }

        /**
         * Calculate the number of rounds to perform based on the set encryption key.
         */
        private int calculateNumRounds() {
            // TODO: Use key to calculate round count
            return 10;
        }

        /**
         * Generate a list of round keys based on the initial key.
         * @return  The list of round keys.
         */
        private List<String[]> performKeyExpansion() {
            numRounds = calculateNumRounds();

            List<String[]> keys = new ArrayList<>();

            for (int i = 0; i < numRounds + 1; i++) {
                keys.add(initialKey);   // TODO: Just use the initial key for now,
                                        // TODO: replace with actual round key.
            }

            return keys;
        }
    }
}
