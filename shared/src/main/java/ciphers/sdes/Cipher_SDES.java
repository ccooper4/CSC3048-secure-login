package ciphers.sdes;
import ciphers.BaseCipher;
import java.util.Arrays;

public class Cipher_SDES extends BaseCipher {

    static int[] plainText = {1,1,0,0,1,0,0,1};
    static int[] cipherText = {1,0,0,0,1,1,1,1};
    static int[] key = {1,1,1,1,1,1,1,1,1,0};
    static int[] ip = {2, 6, 3, 1, 4, 8, 5, 7};
    static int[] ep = {4, 1, 2, 3, 2, 3, 4, 1};
    static int[] inverse_ip = {4, 1, 3, 5, 7, 2, 8, 6};
    static int[] p10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    static int[] p8 = {6, 3, 7, 4, 8, 5, 10, 9};
    static int[] p4 = {2, 4, 3, 1};


    static int[][] sBox1 = {  {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2} };

    static int[][] sBox2 = {  {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3} };

    static int[] temp10 = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    static int[] p4Result = { -1, -1, -1, -1 };
    static int[] f1Result = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    static int[] f2Result = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    static int[] k1 = { -1, -1, -1, -1, -1, -1, -1, -1 };
    static int[] k2 = { -1, -1, -1, -1, -1, -1, -1, -1 };
    static int[] IPP = { -1, -1, -1, -1, -1, -1, -1, -1 };
    static int[] left_nibble = {-1, -1, -1, -1};
    static int[] right_nibble = {-1, -1, -1, -1};

    /**
     * Encryption method
     * @param notUsed
     * @return
     */
    public String encrypt(String notUsed) {
        System.out.println("\tPlaintext  = " + Arrays.toString(plainText));

        keyGeneration();
        initialPermutation();
        f1Result = kFunction(k1);
        f1Result = f1(f1Result);
        //swap module stage
        System.arraycopy(f1Result, 0, right_nibble, 0, f1Result.length / 2);
        System.arraycopy(f1Result, 4, left_nibble, 0, f1Result.length / 2);

        for (int i = 4; i < IPP.length; i++) {
            IPP[i] = right_nibble[i-4]; //+4 to give us right most half
        }

        f2Result = kFunction(k2);
        f2Result = f2(f2Result);
        f2Result = inversePermutation(f2Result);

        System.out.println("\tCiphertext = " + Arrays.toString(f2Result));
        return Arrays.toString(f2Result);
    }

    /**
     * Decryption method
     * @param cText
     * @return
     */
    public String decrypt(String cText) {
        applyPermutation(cipherText, ip, IPP);
        keyGeneration();
        f2Result = kFunction(k2);
        f2Result = f1(f2Result);
        System.arraycopy(f2Result, 0, right_nibble, 0, f2Result.length / 2);
        System.arraycopy(f2Result, 4, left_nibble, 0, f2Result.length / 2);

        for (int i = 4; i < IPP.length; i++) {
            IPP[i] = right_nibble[i-4]; //+4 to give us right most half
        }
        f1Result = kFunction(k1);
        f1Result = f2(f1Result);
        f1Result = inversePermutation(f1Result);

        System.out.println("\tDecrypted = " + Arrays.toString(f1Result));
        return Arrays.toString(f1Result);
    }

    /**
     * Method to generation k1 & k2
     */
    private void keyGeneration(){
        //take key and apply p10 permutation, store in temp10
        applyPermutation(key, p10, temp10);

        //then split into 2 groups of 5 bits and shift left 1 bit with wrap round
        shiftArrayElementsLeft(temp10, 0, 5, 1);
        shiftArrayElementsLeft(temp10, 5, 5, 1);

        //take temp and apply p8 permutation, store in key1
        applyPermutation(temp10, p8, k1);

        //k2
        //take key and apply p10 permutation, store in temp10
        applyPermutation(key, p10, temp10);

        //then split into 2 groups of 5 bits and shift left 3 bit with wrap round
        shiftArrayElementsLeft(temp10, 0, 5, 3);
        shiftArrayElementsLeft(temp10, 5, 5, 3);

        //take temp and apply p8 permutation, store in key 2
        applyPermutation(temp10, p8, k2);
    }

    /**
     * The initial Permutation
     */
    private void initialPermutation(){
        applyPermutation(plainText, ip, IPP);
        System.out.println("IP: " + Arrays.toString(IPP));
    }

    /**
     * Method for k function for both rounds
     * @param key
     * @return
     */
    private int[] kFunction(int[] key){
        int[] rightIPP = new int[4];
        int[] temp1 = new int[8];
        int[] temp2 = new int[8];
        int[] sboxCombined = new int[4];

        for (int i = 0; i < rightIPP.length; i++) {
            rightIPP[i] = IPP[i + 4]; //+4 to give us right most half
        }

        //apply ep to right nibble, store in temp1 *only right most half*
        applyPermutation(rightIPP, ep, temp1);
        System.out.println("EP: " + Arrays.toString(temp1));

        //XOR with key
        XOR(temp1, key, temp1);
        System.out.println("XOR with key: " + Arrays.toString(temp1));

        System.arraycopy(temp1, 4, temp2, 0, temp1.length / 2);
        int sbox1 = getSboxValues(sBox1,temp1);
        int sbox2 = getSboxValues(sBox2, temp2);

        String sboxBinary = String.format("%2s", Integer.toBinaryString(sbox1)).replace(' ', '0');
        sboxBinary += String.format("%2s", Integer.toBinaryString(sbox2)).replace(' ', '0');

        System.out.println(sboxBinary);

        //combine the sbox values into array
        int count = 0;
        for (char ch: sboxBinary.toCharArray()) {
            sboxCombined[count] = Integer.parseInt(Character.toString(ch));
            count++;
        }

        //take result from sboxes and apply p4 permutation
        applyPermutation(sboxCombined, p4, p4Result);
        System.arraycopy(p4Result, 0, sboxCombined, 0, p4Result.length);
        System.out.println("P4: " + Arrays.toString(p4Result));
        return sboxCombined;
    }

    /**
     * Second part of round 1
     * @param sboxCombined
     * @return
     */
    private int[] f1(int[] sboxCombined){
        for (int i = 0; i < sboxCombined.length; i++) {
            sboxCombined[i] = (sboxCombined[i] ^ IPP[i]); //left half of initial permutation and XOR with p4result
        }

        int[] temp = new int [4];
        for (int i = 4; i < IPP.length; i++) {
            temp[i - 4] = IPP[i]; //take right half of initial permutation
        }

        int [] result = new int[8];
        System.arraycopy(sboxCombined, 0, result, 0, sboxCombined.length);
        System.arraycopy(temp, 0, result, 4, temp.length);

        return result;
    }

    /**
     * Second part of round 2
     * @param sboxCombined
     * @return
     */
    private int[] f2(int[] sboxCombined){
        for (int i = 0; i < sboxCombined.length; i++) {
            sboxCombined[i] = (sboxCombined[i] ^ left_nibble[i]); //left half of initial permutation and XOR with p4result
        }

        int[] combinedHalves = new int[8];
        System.arraycopy(sboxCombined, 0, combinedHalves, 0, sboxCombined.length);
        System.arraycopy(right_nibble, 0, combinedHalves, 4, right_nibble.length); //combine the XOR of above and right nibble from earlier

        //Now inverse permutation of the combined halves
        return combinedHalves;
    }

    /**
     * Method to get the sbox values in the given row and column
     * @param sBox
     * @param rolCol
     * @return
     */
    private int getSboxValues(int[][] sBox, int[] rolCol){
        //First get sbox row
        String row = Integer.toString(rolCol[0]);
        row += Integer.toString(rolCol[3]);

        //Then get sbox column
        String col = Integer.toString(rolCol[1]);
        col += Integer.toString(rolCol[2]);

        int sboxCol = Integer.parseInt(col, 2);
        int sboxRow = Integer.parseInt(row, 2);
        return sBox[sboxRow][sboxCol];
    }

    /**
     * Method to apply the inverse permutation
     * @param array
     * @return
     */
    private int [] inversePermutation(int[] array){
        int[] destArray = new int[8];
        applyPermutation(array, inverse_ip, destArray);
        System.out.println("IP: " + Arrays.toString(destArray));
        return destArray;
    }

    /**
     * Method to apply a give permutation
     * @param source
     * @param permutation
     * @param destination
     */
    private void applyPermutation(int[] source, int[] permutation, int[] destination) {
        for (int i = 0; i < permutation.length; i++) {
            destination[i] = source[permutation[i] - 1];
        }
    }

    /**
     * Method to shift array elements left
     * @param array
     * @param startElement
     * @param numElement
     * @param shiftLeftAmount
     */
    private void shiftArrayElementsLeft(int[] array, int startElement, int numElement, int shiftLeftAmount) {
        int[] arrayCopy = new int[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        for (int i = startElement; i < startElement + numElement; i++) {
            array[i] = arrayCopy[((i + shiftLeftAmount) % 5) + startElement];
        }
    }

    /**
     * Method to perform XOR
     * @param input1
     * @param input2
     * @param destination
     * @return
     */
    private int[] XOR(int[] input1, int[] input2, int[] destination) {
        for (int i = 0; i < input1.length; i++) {
            destination[i] = (input1[i] ^ input2[i]);
        }
        return destination;
    }

}
