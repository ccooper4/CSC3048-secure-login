package ciphers;

import java.math.BigInteger;

public class CipherUtils {

    /**
     * Shift a binary string a number of places to the left, padding zero to the end.
     * e.g. (00100000) -> (01000000)
     * @param binary    The binary string to shift.
     * @param shift     The number of places to shift.
     * @return          The shifted binary string.
     */
    public static String shiftBinaryStringLeft(String binary, int shift) {
        if (shift > binary.length()) {
            throw new IllegalArgumentException("Shift count greater than available bits: " + shift);
        }

        String shiftedBinary = binary.substring(shift, binary.length());

        for (int i = 0; i < shift; i++) {
            shiftedBinary = shiftedBinary.concat("0");
        }

        return shiftedBinary;
    }

    public static String hex_XOR(String string1, String string2) {
        BigInteger int1 = BigInteger.valueOf(hexToDecimal(string1));
        BigInteger int2 = BigInteger.valueOf(hexToDecimal(string2));

        BigInteger result = int1.xor(int2);

        return getFormattedHexString(Integer.toHexString(result.intValue()));
    }

    /**
     * Perform an exclusive or operation on two binary strings.
     * e.g. (00100000) XOR (11110000) -> (11010000)
     * @param string1   The first binary string.
     * @param string2   The second binary string.
     * @return          The XOR result.
     */
    public static String binary_XOR(String string1, String string2) {
        // Convert binary strings to big ints
        BigInteger int1 = BigInteger.valueOf(Integer.parseInt(string1, 2));
        BigInteger int2 = BigInteger.valueOf(Integer.parseInt(string2, 2));

        // XOR them
        BigInteger result = int1.xor(int2);

        // Return the string result
        return getFormattedBinaryString(Integer.toBinaryString(result.intValue()));
    }

    /**
     * Convert a decimal value to a padded binary string.
     * @param value The decimal value.
     * @return      The binary string representation.
     */
    public static String decimalToBinaryString(int value) {
        return getFormattedBinaryString(Integer.toBinaryString(value));
    }

    /**
     * Convert a hexadecimal string into a binary string.
     * @param hex   The hex string.
     * @return      The binary representation.
     */
    public static String hexToBinaryString(String hex) {
        return decimalToBinaryString(hexToDecimal(hex));
    }

    /**
     * Convert a binary string into a hexadecimal string.
     * @param binary    The binary string.
     * @return          The hex representation.
     */
    public static String binaryStringToHex(String binary) {
        return getFormattedHexString(Integer.toHexString(Integer.parseInt(binary, 2)));
    }

    /**
     * Convert a hexadecimal string into a decimal number.
     * @param hex   The hex string.
     * @return      The number representation.
     */
    public static int hexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static String getFormattedHexString(String hex) {
        return String.format("%2s", hex).replace(' ', '0');
    }

    public static String getFormattedBinaryString(String binary) {
        return String.format("%8s", binary).replace(' ', '0');
    }
}