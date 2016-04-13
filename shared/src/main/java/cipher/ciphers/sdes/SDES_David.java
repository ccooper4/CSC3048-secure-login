package cipher.ciphers.sdes;

import cipher.ciphers.BaseCipher;

import java.util.Arrays;

/**
 *
 * @author Dave
 */
public class SDES_David extends BaseCipher {

    static int[] plainText = {1, 0, 1, 1, 1, 1, 0, 1};
    static int[] key = {1, 0, 1, 0, 0, 0, 0, 0, 1, 0};
    static int[] ip = {2, 6, 3, 1, 4, 8, 5, 7};
    static int[] ep = {4, 1, 2, 3, 2, 3, 4, 1};
    static int[] inverse_ip = {4, 1, 3, 5, 7, 2, 8, 6};
    static int[] p10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    static int[] p8 = {6, 3, 7, 4, 8, 5, 10, 9};
    static int[] p4 = {2, 4, 3, 1};

    static int[][] s0 = {{1, 0, 3, 2},
    {3, 2, 1, 0},
    {0, 2, 1, 3},
    {3, 1, 3, 2}
    };

    static int[][] s1 = {{0, 1, 2, 3},
    {2, 0, 1, 3},
    {3, 0, 1, 0},
    {2, 1, 0, 3}
    };

    static int[] temp10 = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    static int[] temp8_1 = {-1, -1, -1, -1, -1, -1, -1, -1};
    static int[] temp8_2 = {-1, -1, -1, -1, -1, -1, -1, -1};
    static int[] temp4_1 = {-1, -1, -1, -1};
    static int[] temp4_2 = {-1, -1, -1, -1};
    static int[] temp4_3 = {-1, -1, -1, -1};
    static int tmp = -1;
    static int tmp2 = -1;
    static int tmp3 = -1;

    static int[] k1 = {-1, -1, -1, -1, -1, -1, -1, -1};
    static int[] k2 = {-1, -1, -1, -1, -1, -1, -1, -1};
    static int[] IPP = {-1, -1, -1, -1, -1, -1, -1, -1};
    static int[] EPR = {-1, -1, -1, -1, -1, -1, -1, -1};

    static int[] left_nibble = {-1, -1, -1, -1};
    static int[] right_nibble = {-1, -1, -1, -1};

    @Override
    public String encrypt(String notUsed) {
        String[] args = {};
        return main(args);
    }

    @Override
    public String decrypt(String notUsed) {
        return "notImplemented";
    }

    public static String main(String[] args) {

        //step one generate keys k1, k2
        //k1
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

        //Do initial permutation with plaintext and ip, store in IPP  
        applyPermutation(plainText, ip, IPP);

        //Do function k       
        for (int i = 0; i < temp4_1.length; i++) {
            temp4_1[i] = IPP[i + 4];
        }

        //apply ep to right nibble, stroe in temp8_1
        applyPermutation(temp4_1, ep, temp8_1);

        //XOR with k1
        XOR(temp8_1, k1, temp8_1);
        
        //duplicate temp8_1 into temp8_2
        System.arraycopy(temp8_1, 0, temp8_2, 0, temp8_1.length);

        //Row (1,4)
        rowColumnNibbles(temp8_1, 0, 3);

        //Col (2,3)
        rowColumnNibbles(temp8_2, 1, 2);

        //get sbox numbers
        getSboxNumbers();

        decimalToBinary(temp8_1, 2, 3);
        decimalToBinary(temp8_1, 6, 7);

        combineSboxNumbers(temp8_1);

        //take temp4(sboxes) and apply p4 permutation
        applyPermutation(temp4_1, p4, temp4_1);

        //need to apply fk1(l,r)
        for (int i = 0; i < temp4_1.length; i++) {
            temp4_1[i] = (int) (temp4_1[i] ^ IPP[i]);
        }
        for (int i = 4; i < IPP.length; i++) {
            temp4_2[i - 4] = IPP[i];
        }

        //swap module stage
        System.arraycopy(temp4_1, 0, right_nibble, 0, temp4_1.length);
        System.arraycopy(temp4_2, 0, left_nibble, 0, temp4_1.length);

        //Do function frK2
        //get right nibble       
        System.arraycopy(temp4_2, 0, temp4_1, 0, temp4_1.length);

        //apply ep to right nibble, store in temp8_1   
        applyPermutation(temp4_1, ep, temp8_1);

        //XOR with k2
        XOR(temp8_1, k2, temp8_1);

        System.arraycopy(temp8_1, 0, temp8_2, 0, temp8_1.length);

        //Row (1,4), left nibble
        if (temp8_1[0] + temp8_1[3] == 2) {
            temp8_1[0] = 11;
            temp8_1[1] = 3;
        } else if (temp8_1[0] + temp8_1[3] == 0) {
            temp8_1[0] = 00;
            temp8_1[1] = 0;
        } else if (temp8_1[0] == 1) {
            temp8_1[0] = 10;
            temp8_1[1] = 2;
        } else {
            temp8_1[0] = 01;
            temp8_1[1] = 1;
        }
        temp8_1[2] = -1;
        temp8_1[3] = -1;
        //Row (1,4), right nibble
        if (temp8_1[4] + temp8_1[7] == 2) {
            temp8_1[4] = 11;
            temp8_1[5] = 3;
        } else if (temp8_1[4] + temp8_1[7] == 0) {
            temp8_1[4] = 00;
            temp8_1[5] = 0;
        } else if (temp8_1[4] == 1) {
            temp8_1[4] = 10;
            temp8_1[5] = 2;
        } else {
            temp8_1[4] = 01;
            temp8_1[5] = 1;
        }
        temp8_1[6] = -1;
        temp8_1[7] = -1;

        //Col (2,3), left nibble
        if (temp8_2[1] + temp8_2[2] == 2) {
            temp8_2[0] = 11;
            temp8_2[1] = 3;
        } else if (temp8_2[1] + temp8_2[2] == 0) {
            temp8_2[0] = 00;
            temp8_2[1] = 0;
        } else if (temp8_2[1] == 1) {
            temp8_2[0] = 10;
            temp8_2[1] = 2;
        } else {
            temp8_2[0] = 01;
            temp8_2[1] = 1;
        }
        temp8_2[2] = -1;
        temp8_2[3] = -1;
        //Col (5,6), right nibble
        if (temp8_2[5] + temp8_2[6] == 2) {
            temp8_2[4] = 11;
            temp8_2[5] = 3;
        } else if (temp8_2[5] + temp8_2[6] == 0) {
            temp8_2[4] = 00;
            temp8_2[5] = 0;
        } else if (temp8_2[5] == 1) {
            temp8_2[4] = 10;
            temp8_2[5] = 2;
        } else {
            temp8_2[4] = 01;
            temp8_2[5] = 1;
        }
        temp8_2[6] = -1;
        temp8_2[7] = -1;

        //get sbox numbers
        temp8_1[2] = s0[temp8_1[1]][temp8_2[1]];
        temp8_1[6] = s1[temp8_1[5]][temp8_2[5]];

        binaryToDecimal(temp8_1, 2);

        binaryToDecimal(temp8_1, 6);

        switch (temp8_1[3]) {
            case 11:
                temp4_1[0] = 1;
                temp4_1[1] = 1;
                break;
            case 10:
                temp4_1[0] = 1;
                temp4_1[1] = 0;
                break;
            case 1:
                temp4_1[0] = 0;
                temp4_1[1] = 1;
                break;
            default:
                temp4_1[0] = 0;
                temp4_1[1] = 0;
                break;
        }

        switch (temp8_1[7]) {
            case 11:
                temp4_1[2] = 1;
                temp4_1[3] = 1;
                break;
            case 10:
                temp4_1[2] = 1;
                temp4_1[3] = 0;
                break;
            case 1:
                temp4_1[2] = 0;
                temp4_1[3] = 1;
                break;
            default:
                temp4_1[2] = 0;
                temp4_1[3] = 0;
                break;
        }

        //take temp4(sboxes) and apply p4 permutation
        applyPermutation(temp4_1, p4, temp4_1);

        //function fk2(L,R)
        //need to apply fk1(l,r)
        //get left nibble
        for (int i = 0; i < left_nibble.length; i++) {
            left_nibble[i] = (int) (left_nibble[i] ^ temp4_1[i]);
        }

        for (int i = 0; i < left_nibble.length; i++) {
            temp8_1[i] = left_nibble[i];
        }
        for (int i = 0; i < right_nibble.length; i++) {
            temp8_1[i + 4] = right_nibble[i];
        }
        System.arraycopy(temp8_1, 0, temp8_2, 0, temp8_1.length);

        //apply inverese permutation
        //take temp8_2 and apply inverse permutation, store in temp8_1
        applyPermutation(temp8_2, inverse_ip, temp8_1);

        return Arrays.toString(temp8_1);
    }

    public static void binaryToDecimal(int[] array, int pos) {
        switch (array[pos]) {
            case 3:
                array[pos + 1] = 11;
                break;
            case 2:
                array[pos + 1] = 10;
                break;
            case 1:
                array[pos + 1] = 1;
                break;
            default:
                array[pos + 1] = 0;
                break;
        }
    }

    public static void decimalToBinary(int[] array, int srcPos, int destPos) {
        switch (array[srcPos]) {
            case 3:
                array[destPos] = 11;
                break;
            case 2:
                array[destPos] = 10;
                break;
            case 1:
                array[destPos] = 1;
                break;
            default:
                array[destPos] = 0;
                break;
        }
    }

    public static void applyPermutation(int[] source, int[] permutation, int[] destination) {
        for (int i = 0; i < permutation.length; i++) {
            destination[i] = source[permutation[i] - 1];
        }
    }

    public static void shiftArrayElementsLeft(int[] array, int startElement, int numElement, int shiftLeftAmount) {

        int[] arrayCopy = new int[array.length];

        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        for (int i = startElement; i < startElement + numElement; i++) {
            array[i] = arrayCopy[((i + shiftLeftAmount) % 5) + startElement];
        }
    }

    public static void rowColumnNibbles(int[] array, int ele1, int ele2) {
        //Row (1,4), left nibble
        if (array[ele1] + array[ele2] == 2) {
            array[0] = 11;
            array[1] = 3;
        } else if (array[ele1] + array[ele2] == 0) {
            array[0] = 00;
            array[1] = 0;
        } else if (array[ele1] == 1) {
            array[0] = 10;
            array[1] = 2;
        } else {
            array[0] = 01;
            array[1] = 1;
        }
        ele1 = ele1 + 4;
        ele2 = ele2 + 4;
        //Row (1,4), right nibble
        if (array[ele1] + array[ele2] == 2) {
            array[4] = 11;
            array[5] = 3;
        } else if (array[ele1] + array[ele2] == 0) {
            array[4] = 00;
            array[5] = 0;
        } else if (array[ele1] == 1) {
            array[4] = 10;
            array[5] = 2;
        } else {
            array[4] = 01;
            array[5] = 1;
        }
    }

    public static void getSboxNumbers() {
        temp8_1[2] = s0[temp8_1[1]][temp8_2[1]];
        temp8_1[6] = s1[temp8_1[5]][temp8_2[5]];
    }

    public static void combineSboxNumbers(int[] array) {
        switch (array[3]) {
            case 11:
                temp4_1[0] = 1;
                temp4_1[1] = 1;
                break;
            case 10:
                temp4_1[0] = 1;
                temp4_1[1] = 0;
                break;
            case 1:
                temp4_1[0] = 0;
                temp4_1[1] = 1;
                break;
            default:
                temp4_1[0] = 0;
                temp4_1[1] = 0;
                break;
        }

        switch (array[7]) {
            case 11:
                temp4_1[2] = 1;
                temp4_1[3] = 1;
                break;
            case 10:
                temp4_1[2] = 1;
                temp4_1[3] = 0;
                break;
            case 1:
                temp4_1[2] = 0;
                temp4_1[3] = 1;
                break;
            default:
                temp4_1[2] = 0;
                temp4_1[3] = 0;
                break;
        }
    }

    public static void XOR(int[] input1, int[] input2, int[] destination) {
        for (int i = 0; i < input1.length; i++) {
            destination[i] = (int) (input1[i] ^ input2[i]);
        }
    }

}
