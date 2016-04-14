package cipher.ciphers.hill;

import cipher.ciphers.BaseCipher;
import cipher.util.CipherUtils;
import java.util.ArrayList;
import java.util.Arrays;
import org.jboss.logging.annotations.LogMessage;

/**
 * @author David
 */
public class Cipher_Hill extends BaseCipher {

    private static int matrixDimension = 3;

    private static int[][] key = new int[matrixDimension][matrixDimension];
    private static int[][] keyInverse = new int[matrixDimension][matrixDimension];

    private static int blockPos = 0;
    private static int[] block = new int[matrixDimension];
    private static int[] blockSum = new int[matrixDimension];

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

    public void setKey(int[][] key) {
        Cipher_Hill.key = key;
        setKeyInverse(key);
    }

    public void setKeyInverse(int[][] key) {
        try {
            Cipher_Hill.keyInverse = MatrixOperations.inverse(key);
        } catch (Exception e) {
            logMessage("Cannot invert key. " + Arrays.deepToString(key));
            Cipher_Hill.keyInverse = null;
        }                
    }    
    
    public int getMatrixDimension() {
        return matrixDimension;
    }

    public void setMatrixDimension(int matrixDimension) {
        Cipher_Hill.matrixDimension = matrixDimension;
    }

    @Override
    public String decrypt(String cipherText) {
        if (Cipher_Hill.keyInverse == null) {
            return "Cannot decrypt as the provided key is not invertible yet";
        } else {
            return process(cipherText, keyInverse, "Encrypted cipherText");
        }
    }

    @Override
    public String encrypt(String plainText) {
        return process(plainText, key, "Encrypting plainText");
    }

    public String process(String text, int[][] key, String conversion) {
        String result = "";
        ArrayList<Integer> spacePositions = new ArrayList<>();

        char chr;

        block = new int[matrixDimension];
        blockSum = new int[matrixDimension];

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

        //add spaces back into the cipher, this is optional
        for (Integer spacePosition : spacePositions) {
            result = result.substring(0, spacePosition) + " " + result.substring(spacePosition, result.length());
        }

        log(text, result, conversion);

        return result;

    }

}
