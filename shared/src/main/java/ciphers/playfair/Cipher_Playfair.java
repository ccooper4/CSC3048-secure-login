package ciphers.playfair;

import ciphers.BaseCipher;
import ciphers.CipherUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Dave
 */
public class Cipher_Playfair extends BaseCipher {

    static String keyword = "monarchy";

    static String plainText = "balloon";

    static String cipherText = "";

    static char[][] matrix2D;
    static char[] matrix;

    static boolean[] alpha;

    static int matrixSize = 5;

    static ArrayList<Integer> spacePositions = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static String main(String[] args) {

        alpha = new boolean[26];
        matrix = new char[26];

        matrix2D = new char[matrixSize][matrixSize];

        keyword = removeDuplicateChars(keyword);

        System.out.println("\nStarting PlayfairCipher_David");
        System.out.println("\tKeyword    = " + keyword);
        System.out.println("\tPlainText  = " + plainText);

        plainText = padSeqChars(plainText);
        //pad to even
        if (plainText.length() % 2 == 1) {
            plainText += "x";
        }
        //log spaces
        for (int i = 0; i < plainText.length(); i++) {
            spacePositions.add(i);
        }

        //put keyword into matrix
        for (int i = 0; i < keyword.length(); i++) {
            matrix[i] = keyword.charAt(i);
            alpha[CipherUtils.charToDigit(keyword.charAt(i))] = true;
        }
        //fill matrix with unused letters
        for (int i = keyword.length(); i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (!alpha[j]) {
                    //System.out.println(digitToChar(j) + " has not been used");
                    //group i/j together
                    if ((j == 8) || (j == 9)) {
                        alpha[8] = true;
                        alpha[9] = true;
                    }
                    matrix[i] = CipherUtils.digitToChar(j);
                    alpha[j] = true;
                    break;
                }
            }
        }

        //convert to 2d matrix       
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix2D[i][j] = matrix[(i * 5) + j];
            }
        }

        //take in blocks of 2
        //no doubles like ll in ball
        for (int s = 0; s < plainText.length(); s += 2) {
            char chrA = plainText.charAt(s);
            char chrB = plainText.charAt(s + 1);

            int chrAi = 0;
            int chrAj = 0;
            int chrBi = 0;
            int chrBj = 0;

            if (chrA == chrB) {
                chrB = 'x';
            }

            //find pos of chars
            for (int i = 0; i < matrixSize; ++i) {
                for (int j = 0; j < matrixSize; ++j) {
                    if (matrix2D[i][j] == chrA) {
                        chrAi = i;
                        chrAj = j;
                        //System.out.println("found chrA " + chrA + " at " + i + "," + j);
                    }
                    if (matrix2D[i][j] == chrB) {
                        chrBi = i;
                        chrBj = j;
                        //System.out.println("found chrB " + chrB + " at " + i + "," + j);
                    }
                }
            }

            //same row
            if (chrAi == chrBi) {
                chrA = matrix2D[chrAi][(chrAj + 1) % 5];
                chrB = matrix2D[chrBi][(chrBj + 1) % 5];
            } //same column
            else if (chrAj == chrBj) {
                chrA = matrix2D[(chrAi + 1) % 5][chrAj];
                chrB = matrix2D[(chrBi + 1) % 5][chrBj];
            } else {
                chrA = matrix2D[chrAi][chrBj];
                chrB = matrix2D[chrBi][chrAj];
            }

            cipherText += chrA + "" + chrB;

            //same col take below
            //same row take right
            //other take right, take left
        }

        //System.out.println(Arrays.toString(matrix));
        //System.out.println(Arrays.deepToString(matrix2D));
        System.out.println("\tCiphertext = " + cipherText);
        System.out.println("Ending PlayfairCipher_David");
        
        return cipherText;

    }

    public static String padSeqChars(String str) {

        //System.out.println("\tconverting from " + str);

        for (int s = 0; s < plainText.length() - 1; s += 2) {
            char chrA = plainText.charAt(s);
            char chrB = plainText.charAt(s + 1);

            if (chrA == chrB) {
                str = str.substring(0, s + 1) + "x" + str.substring(s + 1, str.length());
                s--;
            }
        }

        //System.out.println("\tconverted to " + str);

        return str;
    }

    public static String removeDuplicateChars(String str) {

        Set<Character> set = new LinkedHashSet<>();
        for (char c : str.toCharArray()) {
            set.add(c);
        }

        String ret = "";

        for (Character character : set) {
            ret += character;
        }

        return ret;
    }

    /**
     *
     * @param plaintext
     * @return
     */
    @Override
    public String encrypt(String plaintext) {
        String[] args = {plaintext};
        String cipherText = main(args);
        logEncryption(plaintext, cipherText);
        return cipherText;
    }

    /**
     *
     * @param encryptedText
     * @return
     */
    @Override
    public String decrypt(String encryptedText) {
        String output = null;
        logDecryption(encryptedText, output);
        return null;
    }
}
