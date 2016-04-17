package cipher.ciphers.aes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Key expansion code for AES
 *
 * @author David Fee
 */
public class KeyExpansion_AES {

    private static final int[] ROTATEPERMUTATION = {2, 3, 4, 1};

    /**
     * main key expansion method
     *
     * @param originalKey initial algorithm key
     * @param Nk number of keys
     * @param Nr Number of rounds
     * @param Nb Number of blocks
     * @return list of each key for each round
     */
    public List<String[][]> keyExpansion(String[][] originalKey, int Nk, int Nr, int Nb) {

        String parsedString = "";

        for (String[] strings : originalKey) {
            for (String string : strings) {
                parsedString += string;
            }
        }

        return keyExpansion(parsedString, Nk, Nr, Nb);
    }

    private List<String[][]> keyExpansion(String key, int Nk, int Nr, int Nb) {

        String w[] = new String[44];
        String temp;

        int i = 0;
        //LOAD INITIAL KEY
        while (i < Nk) {
            w[i] = key.substring(8 * i, (8 * i) + 8);
            i++;
        }

        i = Nk; //4
        
        String rcon = "";
        for (int j = 1; j < 10; j++) {
            rcon = generateRcon(i, Nk);
        }       

        //num rounds*num blocks
        while (i < Nb * (Nr + 1)) {
            
            rcon = generateRcon(i, Nk);

            temp = w[i - 1];

            if (i % Nk == 0) {

                temp = applyRotation(temp, ROTATEPERMUTATION);

                temp = applySubWord(temp);
                
                temp = applyXorToSubwordAndRcon(temp, rcon);

            } else if ((Nk > 6) && (i % Nk == 4)) {
                //temp = SubWord(temp);
            }

            w[i] = bigBinaryToHex(xorStrings(temp, w[i - Nk]));

            i++;

        }

        return chopArrays(Nk, Nr, w);

    }

    private List<String[][]> chopArrays(int Nk, int Nr, String[] w) {

        String[][] w_final = new String[44][4];

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

        ArrayList<String[][]> res = new ArrayList<>();

        String[][] tmp;

        for (int j = 0; j < Nr + 1; j++) {
            tmp = new String[4][4];

            for (int k = 0; k < Nk; k++) {
                tmp[k][0] = w_final[(j * 4) + k][0];
                tmp[k][1] = w_final[(j * 4) + k][1];
                tmp[k][2] = w_final[(j * 4) + k][2];
                tmp[k][3] = w_final[(j * 4) + k][3];
            }

            res.add(tmp);
        }

        return res;
    }

    public String generateRcon(int round, int nK) {

        //to calculate do left-shift followed with a conditional XOR with a constant
        //rcon = (rcon left shift) ^ (0x11b & -(rcon>>7));
        //Convert to binary
        //• If leftmost bit is zero, shift left
        //• Iif leftmost bit is one, shift left and XOR with 00011011
        //• Convert back to hex        
        String res = "";
        
        int val = round / nK;
        
        round = val;

        int tmp = (int) Math.pow(2, round - 1);

        String binString = Integer.toBinaryString(tmp);
        while (binString.length() < 8) {
            binString = "0" + binString;
        }

        if (round > 8) {

            //System.out.println("\tbinString = " + binString);
            if (binString.charAt(0) == '0') {
                binString = binString.substring(round - 8);
            } else {
                binString = binString.substring(round - 8);
                if (round == 10) {
                    binString = generateRcon((round - 1) * 4, nK);
                    //System.out.println("got previous rcon value = " + binString);
                    binString = hexToBinary(binString.substring(0, 2)) + "0";
                    //System.out.println("updated to = " + binString);
                    binString = binString.substring(1);
                    //System.out.println("round 10 binstring now = " + binString);
                } else {
                    binString = xorBinaryStrings(binString, "00011011");
                }
            }
        } else {
            //System.out.println("\tbinString = " + binString);
        }

        while (binString.length() < 8) {
            binString = "0" + binString;
        }

        String thp = binaryToHex(binString.substring(0, 4));
        res = thp + binaryToHex(binString.substring(4, 8)) + "000000";

        //System.out.println("\tres = " + res);

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
