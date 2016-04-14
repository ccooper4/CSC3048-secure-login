package cipher.ciphers.hill;

import cipher.ciphers.BaseCipher;
import cipher.util.CipherUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**Hill cipher code
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

    /** Default constructor for Hill cipher using assignment provided key
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

    /**Custom constructor for Hill cipher using parameter provided key
     *
     * @param key is the key provided for the cipher
     */
    public Cipher_Hill(int[][] key) {
        setKey(key);
    }

    /**Sets the key for the Hill cipher using the passed param key
     *
     * @param key is the key provided for the cipher
     */
    public void setKey(int[][] key) {
        Cipher_Hill.key = key;
        setKeyInverse(key);
    }

    /**Sets the inverse key for the Hill cipher using the passed param key
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
    
    /** gets the matrixDimension
     *
     * @return
     */
    public int getMatrixDimension() {
        return matrixDimension;
    }

    /** Sets the matric dimension
     *
     * @param matrixDimension the square size of the matrix
     */
    public void setMatrixDimension(int matrixDimension) {
        Cipher_Hill.matrixDimension = matrixDimension;
    }

    /**Method to decrypt encrypted text
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

    /**Method to encrypt plaint text
     *
     * @param plainText is the text to be encrypted
     * @return returns the encrypts cipherText
     */
    @Override
    public String encrypt(String plainText) {
        System.out.println("Encrpyting : " + plainText);
        return process(plainText, key, "Encrypting plainText");
    }

    /**Method is used to apply a key cipher to the text
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
            }
        }
        
        //padding
        while ((initalTextLength + additional) % matrixDimension != 0) {  
            text = text + "a";
            additional++;
        }

        block = new int[matrixDimension];
        blockSum = new int[matrixDimension];

        for (int pos = 0; pos < text.length(); pos++) {

            //for each char in the plainText
            chr = text.charAt(pos);
            System.out.println("cahr:"+chr);

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
                System.out.println("in");

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
        
        result = result.substring(0, initalTextLength - 1);

        log(text, result, conversion);
        
        return result;

    }

}
