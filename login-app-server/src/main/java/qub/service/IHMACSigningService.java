package qub.service;

/**
 * Outlines a service that can HMAC Sign a string.
 */
public interface IHMACSigningService {

    /**
     * Generates a HMAC Signature for the message.
     * @param message The message.
     * @return The Signature.
     */
    byte[] hmacSign(byte[] message);
}
