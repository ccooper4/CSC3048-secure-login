package ciphers.rsa;

import ciphers.BaseCipher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An implementation of the RSA Symmetric algorithm.
 */
public class Cipher_RSA extends BaseCipher {

    //region Fields

    /**
     * The P value for RSA.
     */
    private static final int p = 29;

    /**
     * The Q value for RSA.
     */
    private static final int q = 41;

    /**
     * The D value for RSA.
     */
    private static final int d = 83;

    /**
     * The character map.
     */
    private static final Map<Character , Integer> characterMap = new HashMap<Character, Integer>() {{
        put('A', 1);
        put('B', 2);
        put('C', 3);
        put('D', 4);
        put('E', 5);
        put('F', 6);
        put('G', 7);
        put('H', 8);
        put('I', 9);
        put('J', 10);
        put('K', 11);
        put('L', 12);
        put('M', 13);
        put('N', 14);
        put('O', 15);
        put('P', 16);
        put('Q', 17);
        put('R', 18);
        put('S', 19);
        put('T', 20);
        put('U', 21);
        put('V', 22);
        put('W', 23);
        put('X', 24);
        put('Y', 25);
        put('Z', 26);
    }};

    //region Abstract class override methods.

    @Override
    public String encrypt(String plaintext) {

        List<String> blocks = mapTextToBlocks(plaintext);

        String output = null;

        logEncryption(plaintext, output);
        return null;
    }

    @Override
    public String decrypt(String encryptedText) {
        String output = null;

        logDecryption(encryptedText, output);
        return null;
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
    private int applyExponentiationBySquaringDivision(int num, int exponent, int n) {

        char[] binaryExponentChars = Integer.toBinaryString(exponent).toCharArray();

        Integer c = 1;

        for (int i = binaryExponentChars.length; i >= 0; i--)
        {
            c = (c * c) % n;

            if (binaryExponentChars[i] == '1') {
                c = (c * num) % n;
            }
        }

        return c;
    }

    /**
     * Takes a string and uses it to create a list of blocks, ready for RSA encryption/decryption.
     * @param plainText The plain text.
     * @return A list of blocks.
     */
    private List<String > mapTextToBlocks(String plainText)
    {
        IntStream characters = plainText.chars();

        List<String> numberChars = characters.mapToObj(i -> String.format("%02d",convertCharToNumber(((char) i)))).collect(Collectors.toList());

        return new ArrayList<String>();
    }

    /**
     * Converts a text block to numbers.
     * @param individualCharacter The character.
     * @return A numerical representation of the character.
     */
    private int convertCharToNumber(char individualCharacter) {

        if (characterMap.containsKey(individualCharacter)) {
            return characterMap.get(individualCharacter);
        }
        else
        {
            //Return a standard ASCII number.
            return (int)individualCharacter;
        }
    }

    /**
     * Converts a text block to numbers.
     * @param individualCharacter The character.
     * @return A numerical representation of the character.
     */
    private char convertNumberToChar(int individualCharacter) {

        for (Map.Entry<Character,Integer> entry : characterMap.entrySet()) {

            if (entry.getValue() == individualCharacter) {
                return entry.getKey();
            }
        }

        //Otherwise, map back via ASCII
        return (char)individualCharacter;
    }

    //endregion
}
