package qub.service.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import qub.service.ICryptoHashingService;
import util.EncryptedLogger;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Implements the IHMACSigning Interface using SHA-256
 */
@Service
public class CryptoHashingService implements ICryptoHashingService {

    //region Fields

    /**
     * The HMAC Impl to use.
     */
    private static final String HMAC_IMPL = "HmacSHA256";

    /**
     * The salt algorithm to use.
     */
    private static final String SALT_GENERATION_IMPL = "SHA1PRNG";

    /**
     * The hashing algorithm to use.
     */
    private static final String HASH_GENERATION_IMPL = "PBKDF2WithHmacSHA1";

    /**
     * The character used to seperate the has.
     */
    private static final String HASH_SEPERATOR = "-";

    /**
     * The number of hashing iterations to use.
     */
    private static final int HASH_ITERATIONS = 1000;

    /**
     * Gets the configured secret key.
     */
    private byte[] hmacSecretKey;

    /**
     * Gets the hmacImpl implementation.
     */
    private Mac hmacImpl;

    /**
     * The logger for this class.
     */
    private EncryptedLogger log = new EncryptedLogger(getClass());

    //endregion

    //region Constructor

    /**
     * Constructs a new HMAC Signing Service.
     */
    public CryptoHashingService() {

        try {

            Resource resource = new ClassPathResource("/application.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            hmacSecretKey = props.getProperty("security.hmac.secretKey").getBytes();

        } catch (IOException e) {
            log.error("Could not construct Crypto Hashing Service", e);
            e.printStackTrace();
        }

        try {
            hmacSecretKey = "lSfAB3j1CkgyRVGKCuXI2o13QBBwiWUYMNAc6o28VbUgrHeIzxhP6cBmRdYKSChSgiY5tiVKSRe".getBytes();
            hmacImpl = Mac.getInstance(HMAC_IMPL);
            hmacImpl.init(new SecretKeySpec(hmacSecretKey, HMAC_IMPL));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Could not construct Crypto Hashing Service", e);
        }

    }

    //endregion

    //region IHMACSigning Implementation.

    /**
     * Generates a HMAC Signature for the message.
     * @param message The message.
     * @return The Signature.
     */
    public byte[] hmacSign(byte[] message) {

        return hmacImpl.doFinal(message);

    }

    /**
     * Generates a password hash, including salt.
     * @param original The original password.
     * @return The hash.
     */
    public String hashString(String original) {

        String finalHash = "";

        byte[] salt = generateHashSalt();

        PBEKeySpec spec = new PBEKeySpec(original.toCharArray(), salt, HASH_ITERATIONS, 64 * 8);

        try {

            SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_GENERATION_IMPL);
            byte[] hash = skf.generateSecret(spec).getEncoded();

            finalHash =  HASH_ITERATIONS + HASH_SEPERATOR + bytesToHex(salt) + HASH_SEPERATOR + bytesToHex(hash);


        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Could not build the hash", e);
        }

        return finalHash;
    }

    /**
     * Generates a password hash, including salt.
     * @param input The provided password.
     * @param hash The original, stored hash.
     * @return The hash.
     */
    public boolean verifyStringAgainstHash(String input, String hash) {

        String[] hashParts = hash.split("\\" + HASH_SEPERATOR);

        boolean hashesMatch = false;

        if (hashParts.length != 3 || hashParts[0].length() == 0 || hashParts[1].length() == 0 || hashParts[2].length() == 0) {

            return false;
        }

        int iterations = Integer.parseInt(hashParts[0]);
        String saltHex = hashParts[1];
        String hashHex = hashParts[2];

        byte[] salt = hexToBytes(saltHex);
        byte[] originalHash = hexToBytes(hashHex);

        PBEKeySpec spec = new PBEKeySpec(input.toCharArray(), salt, iterations, 64 * 8);

        try {

            SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_GENERATION_IMPL);
            byte[] newHash = skf.generateSecret(spec).getEncoded();

            hashesMatch = Arrays.equals(originalHash, newHash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Could not build the hash", e);
        }

        return hashesMatch;
    }

    //endregion

    //region Helper Methods

    /**
     * Generates securely random salt that can be used to hash a password.
     * @return A random salt.
     */
    private byte[] generateHashSalt() {

        SecureRandom sr = null;

        try {
            sr = SecureRandom.getInstance(SALT_GENERATION_IMPL);
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not generate salt", e);
        }

        byte[] salt = new byte[16];

        sr.nextBytes(salt);

        return salt;
    }

    /**
     * Converts a byte array to a hex string.
     * @param binary The binary.
     * @return The hex string.
     */
    private String bytesToHex(byte[] binary) {
        return DatatypeConverter.printHexBinary(binary);
    }

    /**
     * Converts a byte array to a hex string.
     * @param hex
     * @return The byte array
     */
    private byte[] hexToBytes(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }

    //endregion
}
