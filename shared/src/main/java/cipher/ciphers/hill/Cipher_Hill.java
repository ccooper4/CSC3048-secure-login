package cipher.ciphers.hill;

import cipher.ciphers.BaseCipher;
import cipher.util.CipherUtils;

import java.util.ArrayList;

/**
 * @author David
 */
public class Cipher_Hill extends BaseCipher {

    private static final int MATRIXDIMENSION = 3;

    private static int[][] key = new int[MATRIXDIMENSION][MATRIXDIMENSION];
    private static int[][] keyInverse = new int[MATRIXDIMENSION][MATRIXDIMENSION];

    private static int blockPos = 0;
    private static int[] block = new int[MATRIXDIMENSION];
    private static int[] blockSum = new int[MATRIXDIMENSION];

    public Cipher_Hill() {

        int[][] key = {
            {15, 10, 29},
            {8, 17, 23},
            {38, 13, 5}
        };

        setKey(key);
    }

    public Cipher_Hill(int[][] key) {
        setKey(key);
    }

    public static void setKey(int[][] key) {
        Cipher_Hill.key = key;
        setKeyInverse(key);
    }

    public static void setKeyInverse(int[][] key) {
        Cipher_Hill.keyInverse = MatrixOperations.inverse(key);
    }

    @Override
    public String decrypt(String cipherText) {

        return process(cipherText, keyInverse, "Encrypted cipherText");
    }

    @Override
    public String encrypt(String plainText) {

        return process(plainText, key, "Encrypted plainText");

    }

    public String process(String text, int[][] key, String conversion) {

        String result = "";
        ArrayList<Integer> spacePositions = new ArrayList<>();

        char chr;

        block = new int[MATRIXDIMENSION];
        blockSum = new int[MATRIXDIMENSION];

        for (int pos = 0; pos < text.length(); pos++) {

            //for each char in the plainText
            chr = text.charAt(pos);

            //if its not a space then put it in the matrix/block
            if (chr != ' ') {
                block[blockPos] = CipherUtils.charToDigit(chr);
                blockPos++;
            } else {
                //if it is a space then note the position for later
                spacePositions.add(pos);
            }

            //if the block/matrix is full
            if (blockPos == MATRIXDIMENSION) {
                blockPos = 0;

                //for each row in the key
                for (int vert = 0; vert < MATRIXDIMENSION; vert++) {
                    //multiply it by the block/matrix and sum the results
                    for (int i = 0; i < MATRIXDIMENSION; i++) {
                        blockSum[vert] += key[vert][i] * block[i];
                    }
                    //mod the total
                    blockSum[vert] = blockSum[vert] % 26;
                }

                //convert the modded digit to a char and add to cipherText
                for (int i = 0; i < MATRIXDIMENSION; i++) {
                    result += CipherUtils.digitToChar(blockSum[i]);
                    blockSum[i] = 0;
                }
            }
        }

        //add spaces back into the cipher, this is optional
        for (Integer spacePosition : spacePositions) {
            result = result.substring(0, spacePosition) + " " + result.substring(spacePosition, result.length());
        }

        log(text, result, conversion);

        return result;

    }

}
