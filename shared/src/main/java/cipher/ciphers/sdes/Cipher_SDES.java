package cipher.ciphers.sdes;

import cipher.ciphers.BaseCipher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cipher_SDES extends BaseCipher {

    private int[] plainText = {1,1,0,0,1,0,0,1};
    private int[] cipherText = {1,0,0,0,1,1,1,1};
    private int[] key = {1,1,1,1,0,1,1,0,0,0};
    private int[] ip = {2, 6, 3, 1, 4, 8, 5, 7};
    private int[] ep = {4, 1, 2, 3, 2, 3, 4, 1};
    private int[] inverse_ip = {4, 1, 3, 5, 7, 2, 8, 6};
    private int[] p10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private int[] p8 = {6, 3, 7, 4, 8, 5, 10, 9};
    private int[] p4 = {2, 4, 3, 1};

    private final int[][] sBox1 = {  {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2} };

    private final int[][] sBox2 = {  {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3} };

    private int[] temp10 = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private int[] p4Result = { -1, -1, -1, -1 };
    private int[] f1Result = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private int[] f2Result = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private int[] k1 = { -1, -1, -1, -1, -1, -1, -1, -1 };
    private int[] k2 = { -1, -1, -1, -1, -1, -1, -1, -1 };
    private int[] IPP = { -1, -1, -1, -1, -1, -1, -1, -1 };
    private int[] left_nibble = {-1, -1, -1, -1};
    private int[] right_nibble = {-1, -1, -1, -1};

    public ArrayList<int[]> encryptWord(String plainText)
    {
        ArrayList<int[]> encrypted = new ArrayList<>();
        String temp;
        int[] input = new int[8];

        byte[] b = plainText.getBytes(StandardCharsets.US_ASCII);
        for (byte currByte:b) {
            temp = String.format("%8s", Integer.toBinaryString(currByte)).replace(' ', '0');
            int count = 0;
            for (char ch: temp.toCharArray()) {
                input[count] = Integer.parseInt(Character.toString(ch));
                count++;
            }
            encrypted.add(encrypt(input));
        }
        return encrypted;
    }

    /**
     * Method to decrypt a binary ciper input
     * @param cipherText
     * @return
     */
    public String decryptWord(List<int[]> cipherText)
    {
        String word = "";
        for(int i = 0; i < cipherText.size(); i++){
            int [] temp = decrypt(cipherText.get(i));
            String temp2  = Arrays.toString(temp).replace(", ", "");
            temp2 = temp2.replaceAll("\\[", "").replaceAll("\\]","");

            int charCode = Integer.parseInt(temp2, 2);
            word += new Character((char)charCode).toString();
        }

        return word;
    }

    /**
     * Encryption method
     * @param input
     * @return
     */
    public int[] encrypt(int[] input) {
        plainText = input;
        keyGeneration();
        initialPermutation();
        f1Result = kFunction(k1);
        f1Result = functionK1(f1Result);

        //swap module stage
        System.arraycopy(f1Result, 0, right_nibble, 0, f1Result.length / 2);
        System.arraycopy(f1Result, 4, left_nibble, 0, f1Result.length / 2);

        for (int i = 4; i < IPP.length; i++) {
            IPP[i] = right_nibble[i-4]; //+4 to give us right most half
        }

        f2Result = kFunction(k2);
        f2Result = functionK2(f2Result);
        f2Result = inversePermutation(f2Result);

        return f2Result;
    }

    /**
     *
     * @param plaintext
     * @return
     */
    @Override
    public String encrypt(String plaintext) {
        String output;
        ArrayList<int[]> binaryValues = encryptWord(plaintext);

        output = convertBinaryArraysToBinaryString(binaryValues);

        logEncryption(plaintext, output);
        return output;
    }

    /**
     *
     * @param encryptedText
     * @return
     */
    @Override
    public String decrypt(String encryptedText) {
        List<int[]> binaryEntries = convertBinaryStringToBinaryArrays(encryptedText);

        String output = decryptWord(binaryEntries);

        logDecryption(encryptedText, output);
        return output;
    }

    /**
     * Decryption method
     * @param cText
     * @return
     */
    public int[] decrypt(int[] cText) {
        cipherText = cText;
        applyPermutation(cipherText, ip, IPP);
        keyGeneration();
        f2Result = kFunction(k2);
        f2Result = functionK1(f2Result);
        System.arraycopy(f2Result, 0, right_nibble, 0, f2Result.length / 2);
        System.arraycopy(f2Result, 4, left_nibble, 0, f2Result.length / 2);

        for (int i = 4; i < IPP.length; i++) {
            IPP[i] = right_nibble[i-4]; //+4 to give us right most half
        }
        f1Result = kFunction(k1);
        f1Result = functionK2(f1Result);
        f1Result = inversePermutation(f1Result);

        return f1Result;
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

        //XOR with key
        XOR(temp1, key, temp1);

        System.arraycopy(temp1, 4, temp2, 0, temp1.length / 2);
        int sbox1 = getSboxValues(sBox1,temp1);
        int sbox2 = getSboxValues(sBox2, temp2);

        String sboxBinary = String.format("%2s", Integer.toBinaryString(sbox1)).replace(' ', '0');
        sboxBinary += String.format("%2s", Integer.toBinaryString(sbox2)).replace(' ', '0');


        //combine the sbox values into array
        int count = 0;
        for (char ch: sboxBinary.toCharArray()) {
            sboxCombined[count] = Integer.parseInt(Character.toString(ch));
            count++;
        }

        //take result from sboxes and apply p4 permutation
        applyPermutation(sboxCombined, p4, p4Result);
        System.arraycopy(p4Result, 0, sboxCombined, 0, p4Result.length);
        return sboxCombined;
    }

    /**
     * Second part of round 1
     * @param sboxCombined
     * @return
     */
    private int[] functionK1(int[] sboxCombined){
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
    private int[] functionK2(int[] sboxCombined){
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

    /**
     * Convert a binary strings representation from string to array.
     * It is assumed that each binary entry in the string is seperated by a " ".
     * @param binaryStrings The string containing many binary strings.
     * @return              The array representation.
     */
    private List<int[]> convertBinaryStringToBinaryArrays(String binaryStrings) {
        String[] words = binaryStrings.split("\\s+");

        List<int[]> binaryEntries = new ArrayList<>();
        int[] binaryEntry;

        // For all the binary strings
        for (String binaryString : words) {
            binaryEntry = new int[8];

            // For each character in the string - extract int value
            for (int i = 0; i < binaryString.length(); i++){
                String character = String.valueOf(binaryString.charAt(i));
                binaryEntry[i] = Integer.valueOf(character);
            }

            binaryEntries.add(binaryEntry);
        }

        return binaryEntries;
    }

    /**
     * Convert a list of binary array entries (where 1 array = 8 binary bits)
     * to plaintext binary strings
     * @param binaryArrays  The array's of binary
     * @return              The binary string
     */
    private String convertBinaryArraysToBinaryString(ArrayList<int[]> binaryArrays) {

        String binaryStrings = "";

        for (int[] binaryEntry : binaryArrays) {

            // Convert binary array to string
            String binaryString = "";
            for (int bit : binaryEntry) {
                binaryString = binaryString.concat(String.valueOf(bit));
            }

            // Concatenate the result
            binaryStrings = binaryStrings.concat(binaryString + " ");
        }

        return binaryStrings;
    }
}
