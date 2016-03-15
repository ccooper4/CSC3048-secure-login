package ciphers.hill;

import ciphers.BaseCipher;
import java.util.ArrayList;

/**
 * @author Dave
 */
public class Hill_David extends BaseCipher {

    static int matrixDimension = 3;

    static int[][] key = {  {17, 17, 05},
                            {21, 18, 21},
                            {02, 02, 19}
    };

    static int blockPos = 0;
    static int[] block;
    static int[] blockSum;

    static String plainText = "pay more money";
    static String cipherText = "";

    static ArrayList<Integer> spacePositions = new ArrayList<>();

    @Override
    public String encrypt(String plaintext) {
        String[] args = {plaintext};
        return main(args);
    }

    @Override
    public String decrypt(String encryptedText) {
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static String main(String[] args) {

        plainText = args[0];
        
        System.out.println("\nStarting Hill_David");
        System.out.println("\tPlaintext  = " + plainText);

        char chr;

        block = new int[matrixDimension];
        blockSum = new int[matrixDimension];

        for (int pos = 0; pos < plainText.length(); pos++) {

            //for each char in the plainText
            chr = plainText.charAt(pos);

            //if its not a space then put it in the matrix/block
            if (chr != ' ') {
                block[blockPos] = charToDigit(chr);
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
                    cipherText += digitToChar(blockSum[i]);
                    blockSum[i] = 0;
                }
            }
        }        

        //add spaces back into the cipher, this is optional
        spacePositions.stream().forEach((space) -> {
            cipherText = cipherText.substring(0, space) + " " + cipherText.substring(space, cipherText.length());
        });

        //print final cipherText
        System.out.println("\tCiphertext = " + cipherText);
        System.out.println("Ending Hill_David");
        
        return cipherText;

    }

    public static char digitToChar(int i) {
        
        char chr = (char) (i + 97);
        
        return chr;
    }

    public static int charToDigit(char chr) {

        chr = Character.toLowerCase(chr);
        
        int ascii = ((int) chr) - 97;

        return ascii;
    }

}
