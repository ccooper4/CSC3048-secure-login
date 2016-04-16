package qub.service;

/**
 * Outlines a service that can HMAC Sign a string.
 */
public interface ICryptoHashingService {

    /**
     * Generates a HMAC Signature for the message.
     * @param message The message.
     * @return The Signature.
     */
    byte[] hmacSign(byte[] message);

    /**
     * Generates a password hash, including salt.
     * @param original The original password.
     * @return The hash.
     */
    String hashString(String original);

    /**
     * Generates a password hash, including salt.
     * @param input The provided password.
     * @param hash The original, stored hash.
     * @return The hash.
     */
    boolean verifyStringAgainstHash(String input, String hash);
}
