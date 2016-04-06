package ciphers.aes;

/**
 *
 * @author David
 */
public class KeyExpansion_AES {

    static String plainText = null;

    static String initialCipherKey[] = {"2b", "28", "ab", "09", "7e", "ae", "f7", "cf", "15", "d2", "15", "4f", "16", "a6", "88", "3c"};
    static String blankCipherKey[] = {"00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00"};
    static String currentCipherKey[] = null;
    static String roundCipherKeys[][];

    static int numBlocks = 4; //128 (4* 32 bit words), 192 (6*32), 256 (8*32)
    static int numRounds = 10; //10 for 4 blocks, 12 for 6 blocks, 14 for 8 blocks
    static int roundNumber = 0;

    static int rotatePermutation[] = {2, 3, 4, 1};

    static String sbox[][] = {{"63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
    {"ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af", "9c", "a4", "72", "c0"},
    {"b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1", "71", "d8", "31", "15"},
    {"04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2", "eb", "27", "b2", "75"},
    {"09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3", "29", "e3", "2f", "84"},
    {"53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39", "4a", "4c", "58", "cf"},
    {"d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f", "50", "3c", "9f", "a8"},
    {"51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21", "10", "ff", "f3", "d2"},
    {"cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d", "64", "5d", "19", "73"},
    {"60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14", "de", "5e", "0b", "db"},
    {"e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62", "91", "95", "e4", "79"},
    {"e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea", "65", "7a", "ae", "08"},
    {"ba", "78", "25", "2e", "1c", "a6", "b4", "c6", "e8", "dd", "74", "1f", "4b", "bd", "8b", "8a"},
    {"70", "3e", "b5", "66", "48", "03", "f6", "0e", "61", "35", "57", "b9", "86", "c1", "1d", "9e"},
    {"e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9", "ce", "55", "28", "df"},
    {"8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f", "b0", "54", "bb", "16"}
    };

    static String[][] w_final = {{"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""},
    {"", "", "", ""}};

    public static String main(String[] args) {
        plainText = args[0];

        System.out.println("\nStarting AES_David");
        System.out.println("\tPlaintext  = " + plainText);

        System.out.println("\t\tKey Expansion");

        //keyExpansion();
        keyExpansion("2B7E151628AED2A6ABF7158809CF4F3C", 4, numRounds, numBlocks);

        System.out.println("\tCiphertext = ");
        System.out.println("Ending AES_David");

        return "ChangeMe";
    } 

    public static String[][] keyExpansion(String key, int Nk, int Nr, int Nb) {
        String w[] = new String[44];
        String temp = "";

        int i = 0;
        //LOAD INITIAL KEY
        while (i < Nk) {
            w[i] = key.substring(8 * i, (8 * i) + 8);
            i++;
        }
        i = Nk; //4

        //System.out.println(Arrays.deepToString(w));
        while (i < Nb * (Nr + 1)) {
            //System.out.println("\n-------------------------------------------while " + i + " < " + (Nb * (Nr + 1)));
            temp = w[i - 1];
            //System.out.println("temp =  " + temp);
            if (i % Nk == 0) {
                //System.out.println("needs roted");
                temp = applyRotation(temp, rotatePermutation);
                //System.out.println("needs sub worded");
                temp = applySubWord(temp);
                //System.out.println("needs RCon[i/nK]");
                String rcon = generateRcon(i, Nk, 2);
                //System.out.println("needs Xored temp and sub");
                temp = applyXorToSubwordAndRcon(temp, rcon);

            } else if ((Nk > 6) && (i % Nk == 4)) {
                //temp = SubWord(temp);
            }
            //System.out.println("needs Xored temp and w[i-nK]");
            w[i] = bigBinaryToHex(xorStrings(temp, w[i - Nk]));
            //System.out.println("result for round " + i + " = " + w[i]);
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

        //return w;
        return w_final;

    }

    public static String generateRcon(int i, int nK, int x) {

        String res = "";

        //System.out.println("\t Generating Rcon for " + i + "," + nK + "," + x);
        int val = i / nK;
        //val = (int) Math.pow(x, val - 1);

        //System.out.println("\t val i/nK =  " + val);
//        res = Integer.toString(val);
//        for (int k = res.length(); k < 8; k++) {
//            res = "0" + res;
//        }
//        String hex = "";      
//        System.out.println("\t res = " + res);
//        System.out.println("\t hex 1 = " + res.substring(0, 4));
//        System.out.println("\t hex 2 = " + res.substring(4, 8));
//        hex = hex + hexToDecimal(res.substring(0, 4));
//        hex = hex + hexToDecimal(res.substring(4, 8));
//        System.out.println("\tRCon = " + hex + "000000");
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

    public static String xorStrings(String str1, String str2) {
        String res = "";
        String tmp1 = "";
        String tmp2 = "";
        for (int i = 0; i < 4; i++) {
            tmp1 = hexToBinary(str1.substring(i * 2, ((i * 2) + 2)));
            tmp2 = hexToBinary(str2.substring(i * 2, ((i * 2) + 2)));
            //System.err.println("\ttmp1 = " + tmp1);
            //System.err.println("\ttmp2 = " + tmp2);
            res = res + xorBinaryStrings(tmp1, tmp2);
            //System.err.println("\txord = " + res);
            //res = res + ((char) (s.charAt(i) ^ key.charAt(i % key.length())));
        }
        return res;
    }

    public static String applyRotation(String source, int[] rotatePermutation) {
        String res = "";
        String array[] = new String[4];
        String[] copy = new String[4];
        //System.out.println("\t1. APPLYING ROTATION");
        //System.out.println("\tsource = " + source);
        for (int i = 0; i < 4; i++) {
            //System.out.println("\tlooppppp = " + i);
            array[i] = source.substring(i * 2, ((i * 2) + 2));
        }
        System.arraycopy(array, 0, copy, 0, array.length);
        for (int i = 0; i < rotatePermutation.length; i++) {
            array[i] = copy[rotatePermutation[i] - 1];
        }
        //System.out.println("\tfinal array = " + Arrays.deepToString(array));
        for (int i = 0; i < 4; i++) {
            res = res + array[i].toString();
        }
        return res;
    }

    public static String applySubWord(String source) {
        String res = "";
        //System.out.println("\t2. APPLYING SUBWORD ");
        //System.out.println("\tApplying subword to : " + source);
        int x = -1;
        int y = -1;
        for (int i = 0; i < 4; i++) {
//            System.out.println("\ty = " + source.substring(i * 2, i * 2 + 1));
//            System.out.println("\tx = " + source.substring((i * 2 + 1), (i * 2 + 1) + 1));
            y = hexToDecimal(source.substring(i * 2, i * 2 + 1));
            x = hexToDecimal(source.substring((i * 2 + 1), (i * 2 + 1) + 1));
            //System.out.println("\tx, y = " + x + ", " + y);
            res = res + sbox[y][x];
        }

        //System.out.println("\tSubbworded to : " + res);
        return res;
    }

    public static String applyXorToSubwordAndRcon(String subword, String rcon) {
        String tmp = subword;

        //System.out.println("\tgot rcon as = " + rcon);
        //System.out.println("\tgot subword as = " + subword);
        String binaryRcon = hexToBinary(rcon.substring(0, 2));
        String binarySubword = hexToBinary(tmp.substring(0, 2));

        //System.out.println("\tbinary rcon      = " + rcon.substring(0, 2) + " = " + binaryRcon);
        //System.out.println("\tbinary subword   = " + subword.substring(0, 2) + " = " + binarySubword);
        String Xor = "" + xorBinaryStrings(binaryRcon, binarySubword);
//        for (int i = 0; i < 8; i++) {
//            Xor = Xor + (Integer.parseInt(binarySubword.substring(i, i + 1)) ^ Integer.parseInt(rcon.substring(i, i + 1)));
//        }

        subword = binaryToHex(Xor) + tmp.substring(2, 8);

        //System.out.println("\tsubword XOR rcon = " + subword);
        return subword;
    }

    public static String xorBinaryStrings(String str1, String str2) {

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

    public static String hexToBinary(String hex) {

        String binary = Integer.toBinaryString(Integer.parseInt(hex, 16));
        int binLength = 8;
        for (int i = binary.length(); i < binLength; i++) {
            binary = "0" + binary;
        }

        return binary;
    }

    public static int hexToDecimal(String hex) {
        int decimal = Integer.parseInt(hex, 16);
        return decimal;
    }

    public static String binaryToHex(String binary) {
        String hex = Integer.toString(Integer.parseInt(binary, 2), 16);
        return hex;
    }

    public static String bigBinaryToHex(String binary) {
        String hex = "";
        for (int i = 0; i < binary.length(); i = i + 4) {
            hex = hex + "" + Integer.toString(Integer.parseInt(binary.substring(i, i + 4), 2), 16);
        }

        return hex;
    }

    public String encrypt(String plaintext) {
        String[] args = {plaintext};

        return main(args);
    }

    public String[] keyExpander(String key, int Nk, int Nr, int Nb) {
        //return keyExpansion("2B7E151628AED2A6ABF7158809CF4F3C", 4, numRounds, numBlocks);
        return keyExpansion(key, Nk, Nr, Nb);
    }

    public String decrypt(String encryptedText) {
        return null;
    }
}
