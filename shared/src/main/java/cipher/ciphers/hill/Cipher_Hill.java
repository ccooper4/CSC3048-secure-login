package cipher.ciphers.hill;

import cipher.ciphers.BaseCipher;
import cipher.util.CipherUtils;

import java.util.ArrayList;

/**
 * @author David
 */
public class Cipher_Hill extends BaseCipher {
    
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
    
    private static final int MATRIXDIMENSION = 3;

    private static int[][] key = new int[MATRIXDIMENSION][MATRIXDIMENSION];    
    private static int[][] keyInverse = new int[MATRIXDIMENSION][MATRIXDIMENSION];

    private static int blockPos = 0;
    private static int[] block = new int[MATRIXDIMENSION];
    private static int[] blockSum  = new int[MATRIXDIMENSION];
    private static String plainText = "";
    private static String cipherText = "";

    @Override
    public String decrypt(String cipherText) {
        
        plainText = "";
        ArrayList<Integer> spacePositions = new ArrayList<>();

        char chr;
        
        block = new int[MATRIXDIMENSION];
        blockSum = new int[MATRIXDIMENSION];

        for (int pos = 0; pos < cipherText.length(); pos++) {

            //for each char in the cipherText
            chr = cipherText.charAt(pos);

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
                        blockSum[vert] += keyInverse[vert][i] * block[i];
                    }
                    //mod the total
                    blockSum[vert] = blockSum[vert] % 26;
                }

                //convert the modded digit to a char and add to plainText
                for (int i = 0; i < MATRIXDIMENSION; i++) {
                    plainText += CipherUtils.digitToChar(blockSum[i]);
                    blockSum[i] = 0;
                }
            }
        }

        //add spaces back into the cipher
        spacePositions.stream().forEach((space) -> {
            plainText = plainText.substring(0, space) + " " + plainText.substring(space, plainText.length());
        });

        logDecryption(cipherText, plainText);
        return plainText;
    }

    @Override
    public String encrypt(String plainText) {
        
        cipherText = "";
        ArrayList<Integer> spacePositions = new ArrayList<>();

        char chr;

        block = new int[MATRIXDIMENSION];
        blockSum = new int[MATRIXDIMENSION];

        for (int pos = 0; pos < plainText.length(); pos++) {

            //for each char in the plainText
            chr = plainText.charAt(pos);

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
                    cipherText += CipherUtils.digitToChar(blockSum[i]);
                    blockSum[i] = 0;
                }
            }
        }

        //add spaces back into the cipher, this is optional
        spacePositions.stream().forEach((space) -> {
            cipherText = cipherText.substring(0, space) + " " + cipherText.substring(space, cipherText.length());
        });

        logEncryption(plainText, cipherText);
        return cipherText;

    }
}