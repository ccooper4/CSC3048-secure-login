package qub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Implements the IHMACSigning Interface using SHA-256
 */
@Service
public class HMACSigningService implements IHMACSigningService {

    //region Fields

    /**
     * The HMAC Impl to use.
     */
    private static final String HMAC_IMPL = "HmacSHA256";

    /**
     * Gets the configured secret key.
     */
    @Value("${secretKey}")
    private byte[] secretKey;

    /**
     * Gets the hmacImpl implementation.
     */
    private Mac hmacImpl;

    //endregion

    //region Constructor

    /**
     * Constructs a new HMAC Signing Service.
     */
    public HMACSigningService() {

        try {
            hmacImpl = Mac.getInstance(HMAC_IMPL);
            hmacImpl.init(new SecretKeySpec(secretKey, HMAC_IMPL));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
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

    //endregion
}
