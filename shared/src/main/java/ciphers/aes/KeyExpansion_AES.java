package ciphers.aes;

import java.util.List;

public class KeyExpansion_AES {

    private static int rotatePermutation[] = {2, 3, 4, 1};

    public List<String[][]> keyExpansion(String[][] originalKey, int Nk, int Nr, int Nb) {
        String parsedString = ""; // TODO Make the array a string

        for (String[] strings : originalKey) {
            for (String string : strings) {
                parsedString += string;
            }
        }

        return keyExpansion(parsedString, Nk, Nr, Nb);
    }

    private List<String[][]> keyExpansion(String key, int Nk, int Nr, int Nb) {

        String[][] w_final = new String[44][4];

        String w[] = new String[44];
        String temp;

        int i = 0;
        //LOAD INITIAL KEY
        while (i < Nk) {
            w[i] = key.substring(8 * i, (8 * i) + 8);
            i++;
        }

        i = Nk; //4

        while (i < Nb * (Nr + 1)) {

            temp = w[i - 1];

            if (i % Nk == 0) {

                temp = applyRotation(temp, rotatePermutation);

                temp = applySubWord(temp);

                String rcon = generateRcon(i, Nk, 2);

                temp = applyXorToSubwordAndRcon(temp, rcon);

            } else if ((Nk > 6) && (i % Nk == 4)) {
                //temp = SubWord(temp);
            }

            w[i] = bigBinaryToHex(xorStrings(temp, w[i - Nk]));

            i++;

        }

        int count = 0;
        for (String w1 : w) {

            String tmp[] = new String[4];

            tmp[0] = w1.substring(0, 2);
            tmp[1] = w1.substring(2, 4);
            tmp[2] = w1.substring(4, 6);
            tmp[3] = w1.substring(6, 8);

            w_final[count] = tmp.clone();

            count++;
        }
        
        String[][] tmp = new String[4][4];
        List<String[][]> res = null;
        
        for (int j = 0; j < Nr + 1; j++) {
            for (int k = 0; k < Nk; k++) {
                tmp[k][0] = w_final[(j * 4) + k][0];
                tmp[k][1] = w_final[(j * 4) + k][1];
                tmp[k][2] = w_final[(j * 4) + k][2];
                tmp[k][3] = w_final[(j * 4) + k][3];
            }
            res.add(tmp);
        }

        //return w_final;
        return res; 

    }

    private List<String[][]> chopArrays(String[][] arrays, int seperator) {
        return null;
    }

    private String generateRcon(int i, int nK, int x) {

        String res = "";

        int val = i / nK;

        switch (val) {
            case 1:
                res = "01000000";
                break;
            case 2:
                res = "02000000";
                break;
            case 3:
                res = "04000000";
                break;
            case 4:
                res = "08000000";
                break;
            case 5:
                res = "10000000";
                break;
            case 6:
                res = "20000000";
                break;
            case 7:
                res = "40000000";
                break;
            case 8:
                res = "80000000";
                break;
            case 9:
                res = "1B000000";
                break;
            case 10:
                res = "36000000";
                break;

        }

        //return hex + "000000";
        return res;

    }

    private String xorStrings(String str1, String str2) {
        String res = "";
        String tmp1 = "";
        String tmp2 = "";
        for (int i = 0; i < 4; i++) {
            tmp1 = hexToBinary(str1.substring(i * 2, ((i * 2) + 2)));
            tmp2 = hexToBinary(str2.substring(i * 2, ((i * 2) + 2)));
            res = res + xorBinaryStrings(tmp1, tmp2);
        }
        return res;
    }

    private String applyRotation(String source, int[] rotatePermutation) {
        String res = "";
        String array[] = new String[4];
        String[] copy = new String[4];

        for (int i = 0; i < 4; i++) {
            array[i] = source.substring(i * 2, ((i * 2) + 2));
        }

        System.arraycopy(array, 0, copy, 0, array.length);

        for (int i = 0; i < rotatePermutation.length; i++) {
            array[i] = copy[rotatePermutation[i] - 1];
        }

        for (int i = 0; i < 4; i++) {
            res = res + array[i];
        }

        return res;
    }

    private String applySubWord(String source) {
        String res = "";

        int x;
        int y;

        for (int i = 0; i < 4; i++) {
            y = hexToDecimal(source.substring(i * 2, i * 2 + 1));
            x = hexToDecimal(source.substring((i * 2 + 1), (i * 2 + 1) + 1));

            res = res + Cipher_AES.S_BOX[y][x];
        }

        return res;
    }

    private String applyXorToSubwordAndRcon(String subword, String rcon) {
        String tmp = subword;

        String binaryRcon = hexToBinary(rcon.substring(0, 2));
        String binarySubword = hexToBinary(tmp.substring(0, 2));

        String Xor = "" + xorBinaryStrings(binaryRcon, binarySubword);

        subword = binaryToHex(Xor) + tmp.substring(2, 8);

        return subword;
    }

    private String xorBinaryStrings(String str1, String str2) {

        String res = "";

        for (int i = 0; i < str1.length(); i++) {
            if (((Integer.parseInt(str1.substring(i, i + 1))) ^ (Integer.parseInt(str2.substring(i, i + 1)))) == 1) {
                res = res + "1";
            } else {
                res = res + "0";
            }
        }

        return res;
    }

    private String hexToBinary(String hex) {

        String binary = Integer.toBinaryString(Integer.parseInt(hex, 16));
        int binLength = 8;
        for (int i = binary.length(); i < binLength; i++) {
            binary = "0" + binary;
        }

        return binary;
    }

    private int hexToDecimal(String hex) {
        int decimal = Integer.parseInt(hex, 16);
        return decimal;
    }

    private String binaryToHex(String binary) {
        String hex = Integer.toString(Integer.parseInt(binary, 2), 16);
        return hex;
    }

    private String bigBinaryToHex(String binary) {
        String hex = "";
        for (int i = 0; i < binary.length(); i = i + 4) {
            hex = hex + "" + Integer.toString(Integer.parseInt(binary.substring(i, i + 4), 2), 16);
        }

        return hex;
    }
}
