package cipher.ciphers.rsa;

import cipher.ciphers.BaseCipher;

import java.util.*;

/**
 * An implementation of the RSA Asymmetric algorithm.
 */
public class Cipher_RSA extends BaseCipher {

    //region Fields

    /**
     * The P value for RSA. - Chose a larger value of P to handle a larger number range.
     */
    private static final int p = 257;

    /**
     * The Q value for RSA. - Chose a larger value of Q to handle a larger number range.
     */
    private static final int q = 337;

    /**
     * The D value for RSA.
     */
    private static final int d = 17;

    /**
     * The value of E.
     */
    private int e;

    /**
     * The value of N.
     */
    private int n = p*q;

    /**
     * The value of W.
     */
    private int w = (p-1) * (q-1);

    /**
     * The character map.
     */
    private static final Map<Character , String> characterMap = new HashMap<Character, String>() {{
        put(' ', "00");
        put('A', "01");
        put('B', "02");
        put('C', "03");
        put('D', "04");
        put('E', "05");
        put('F', "06");
        put('G', "07");
        put('H', "08");
        put('I', "09");
        put('J', "10");
        put('K', "11");
        put('L', "12");
        put('M', "13");
        put('N', "14");
        put('O', "15");
        put('P', "16");
        put('Q', "17");
        put('R', "18");
        put('S', "19");
        put('T', "20");
        put('U', "21");
        put('V', "22");
        put('W', "23");
        put('X', "24");
        put('Y', "25");
        put('Z', "26");
    }};

    //region Abstract class override methods.

    @Override
    public String encrypt(String plaintext) {

        plaintext = plaintext.toUpperCase();

        String output = "";

        if (plaintext.length() % 2 != 0)
        {
            plaintext += " ";
        }

        e = calculateEFromEuclidGcd(d, w, w);

        for (int i = 0; i < plaintext.length(); i += 2) {

            char blockChar1 = plaintext.charAt(i);
            char blockChar2 = plaintext.charAt(i+1);

            String blockNum1 = convertCharToNumber(blockChar1);
            String blockNum2 = convertCharToNumber(blockChar2);

            String fullBlock = blockNum1 + blockNum2;

            long numBlock = Long.parseLong(fullBlock);

            long cipher = applyExponentiationBySquaringDivision(numBlock, e, n);

            output += Long.toString(cipher) + " ";

        }

        logEncryption(plaintext, output);

        output = output.trim();

        return output;
    }

    @Override
    public String decrypt(String encryptedText) {

        String plainText = "";

        e = calculateEFromEuclidGcd(d, w, w);

        String[] cipherTextBlocks = encryptedText.split("\\ ");

        for (String block : cipherTextBlocks) {

            long numberBlock = Long.parseLong(block);

            long message = applyExponentiationBySquaringDivision(numberBlock, d, n);

            String textBlock = Long.toString(message);

            textBlock = String.format("%04d", Long.parseLong(textBlock));

            String blockPart1 = textBlock.substring(0, 2);
            String blockPart2 = textBlock.substring(2, 4);

            char blockPart1Char = convertNumberToChar(blockPart1);
            char blockPart2Char = convertNumberToChar(blockPart2);

            String finalMessageBlock = Character.toString(blockPart1Char) + Character.toString(blockPart2Char);

            plainText += finalMessageBlock;

            int x = 3;

        }

        logDecryption(encryptedText, plainText);
        return plainText;
    }

    //endregion

    //region Methods

    /**
     * Calculates (num^exponent) mod n using Exponentiation by Squaring/Dividing.
     * @param num The number.
     * @param exponent The power to raise to.
     * @param n The n number from the equation.
     * @return
     */
    public long applyExponentiationBySquaringDivision(long num, long exponent, long n) {

        char[] binaryExponentChars = Long.toBinaryString(exponent).toCharArray();

        long c = 1;

        for (int i = 0; i < binaryExponentChars.length; i++)
        {
            long tmp = c * c;
            c = tmp % n;

            if (binaryExponentChars[i] == '1') {
                long tmp2 = c * num;
                c = tmp2 % n;
            }
        }

        return c;
    }

    /**
     * Converts a text block to numbers.
     * @param individualCharacter The character.
     * @return A numerical representation of the character.
     */
    private String convertCharToNumber(char individualCharacter) {

        return characterMap.get(individualCharacter);

    }

    /**
     * Converts a text block to numbers.
     * @param individualCharacter The character.
     * @return A numerical representation of the character.
     */
    private char convertNumberToChar(String individualCharacter) {

        char rtr = ' ';

        for (Map.Entry<Character,String> entry : characterMap.entrySet()) {

            if (entry.getValue().equals(individualCharacter)) {
                rtr =  entry.getKey();
            }
        }

        return rtr;
    }

    /**
     * Returns the GCD of 2 numbers, as calculated via Euclid's algorithm.
     * @param a The first number.
     * @param b The second number.
     * @return The greatest common divider of A and B.
     */
    public int calculateEFromEuclidGcd(int a, int b, int w) {

        int prevAi = 1;
        int ai = 0;

        int prevBi = 0;
        int bi = 1;

        while (b != 0)
        {
            int quotient = a / b;
            int remainder = a - quotient * b;

            int newAi = prevAi - quotient * ai;
            int newBi = prevBi - quotient * bi;

            prevAi = ai;
            ai = newAi;
            prevBi = bi;
            bi = newBi;

            a = b;
            b = remainder;
        }

        if (prevAi < 0)
        {
            return w + prevAi;
        }
        else
        {
            return prevAi;
        }

    }

    //endregion
}
