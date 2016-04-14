package cipher.ciphers.hill;

import cipher.ciphers.BaseCipher;
import cipher.util.CipherUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hill cipher code
 *
 * @author David Fee
 */
public class Cipher_Hill extends BaseCipher {

    private static int matrixDimension = 3;

    private static int[][] key = new int[matrixDimension][matrixDimension];
    private static int[][] keyInverse = new int[matrixDimension][matrixDimension];

    private static int blockPos = 0;
    private static int[] block = new int[matrixDimension];
    private static int[] blockSum = new int[matrixDimension];

    /**
     * Default constructor for Hill cipher using assignment provided key
     *
     */
    public Cipher_Hill() {
        int[][] key = {
            {15, 10, 29},
            {8, 17, 23},
            {38, 13, 5}
        };
        setKey(key);
    }

    /**
     * Custom constructor for Hill cipher using parameter provided key
     *
     * @param key is the key provided for the cipher
     */
    public Cipher_Hill(int[][] key) {
        setKey(key);
    }

    /**
     * Sets the key for the Hill cipher using the passed param key
     *
     * @param key is the key provided for the cipher
     */
    public void setKey(int[][] key) {
        Cipher_Hill.key = key;
        setKeyInverse(key);
    }

    /**
     * Sets the inverse key for the Hill cipher using the passed param key
     *
     * @param key is the key provided for the cipher
     */
    public void setKeyInverse(int[][] key) {
        try {
            Cipher_Hill.keyInverse = MatrixOperations.inverse(key);
        } catch (Exception e) {
            logMessage("Cannot invert key. " + Arrays.deepToString(key));
            Cipher_Hill.keyInverse = null;
        }
    }

    /**
     * gets the matrixDimension
     *
     * @return
     */
    public int getMatrixDimension() {
        return matrixDimension;
    }

    /**
     * Sets the matric dimension
     *
     * @param matrixDimension the square size of the matrix
     */
    public void setMatrixDimension(int matrixDimension) {
        Cipher_Hill.matrixDimension = matrixDimension;
    }

    /**
     * Method to decrypt encrypted text
     *
     * @param cipherText is the text to be decoded
     * @return returns the unencoded cipherText
     */
    @Override
    public String decrypt(String cipherText) {
        if (Cipher_Hill.keyInverse == null) {
            return "Cannot decrypt as the provided key is not invertible yet";
        } else {
            return process(cipherText, keyInverse, "Encrypted cipherText");
        }
    }

    /**
     * Method to encrypt plaint text
     *
     * @param plainText is the text to be encrypted
     * @return returns the encrypts cipherText
     */
    @Override
    public String encrypt(String plainText) {
        return process(plainText, key, "Encrypting plainText");
    }

    /**
     * Method is used to apply a key cipher to the text
     *
     * @param text is the String on which the key will be applied to
     * @param key is either the key or the inverse key,depends on encrypt/decrpt
     * @param conversion is for providing which way the conversion is being done
     * @return the encrypted/decrypted text
     */
    public String process(String text, int[][] key, String conversion) {
        String result = "";
        ArrayList<Integer> spacePositions = new ArrayList<>();

        int initalTextLength = text.length();
        int additional = 0;

        char chr;

        //count spaces        
        for (int pos1 = 0; pos1 < text.length(); pos1++) {
            chr = text.charAt(pos1);
            if (chr != ' ') {
                additional++;
            } else {
                //if it is a space then note the position for later
                spacePositions.add(pos1);
            }
        }        
        //remove spaces
        text = text.replaceAll("\\s","");

        //padding
        while ((text.length() + additional) % matrixDimension != 0) {
            text = text + "a";
            additional++;
        }

        block = new int[matrixDimension];
        blockSum = new int[matrixDimension];

        for (int pos = 0; pos < text.length(); pos++) {

            //for each char in the plainText
            chr = text.charAt(pos);

            block[blockPos] = CipherUtils.charToDigit(chr);
            blockPos++;

            //if the block/matrix is full
            if (blockPos == matrixDimension) {
                blockPos = 0;

                //for each row in the key
                for (int vert = 0; vert < matrixDimension; vert++) {
                    //multiply it by the block/matrix and sum the results
                    for (int i = 0; i < matrixDimension; i++) {
                        blockSum[vert] += key[vert][i] * block[i];
                    }
                    //mod the total
                    blockSum[vert] = blockSum[vert] % 26;
                }

                //convert the modded digit to a char and add to cipherText
                for (int i = 0; i < matrixDimension; i++) {
                    result += CipherUtils.digitToChar(blockSum[i]);
                    blockSum[i] = 0;
                }
            }
        }

        //add spaces back into the cipher
        for (Integer spacePosition : spacePositions) {
            result = result.substring(0, spacePosition) + " " + result.substring(spacePosition, result.length());
            text = text.substring(0, spacePosition) + " " + text.substring(spacePosition, text.length());
        }

        System.out.println("result = " + result);

        result = result.substring(0, initalTextLength);
        text = text.substring(0, initalTextLength);

        log(text, result, conversion);

        return result;

    }

}
