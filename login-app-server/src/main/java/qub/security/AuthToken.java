package qub.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import qub.domain.user.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Represents the Auth Token used for authentication.
 */
public class AuthToken extends AbstractAuthenticationToken {

    //region Fields

    /**
     * The HMAC Impl to use.
     */
    private static final String HMAC_IMPL = "HmacSHA256";

    /**
     * The character used to seperate the token.
     */
    private static final String TOKEN_SEPERATOR = "-";

    /**
     * Gets the configured secret key.
     */
    private byte[] secretKey;

    /**
     * Gets the user this token represents.
     */
    private User authUser;

    //endregion

    //region Constructor

    /**
     * Constructs a new Auth Token.
     */
    public AuthToken(User authUser) {
        super(null);
        this.authUser = authUser;
    }

    //endregion

    //region Getters & Setters

    /**
     * Sets the secret key.
     * @param secretKey The new secret key.
     */
    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    //endregion

    //region ToString Override

    /**
     * Returns this token as a HMAC Signed, Base-64 encoded token.
     * @return
     */
    @Override
    public String toString() {

        Gson gson = new GsonBuilder().create();

        String jsonObj = gson.toJson(this);

        String finalToken = null;

        try {

            Mac hmac = Mac.getInstance(HMAC_IMPL);
            hmac.init(new SecretKeySpec(secretKey, HMAC_IMPL));

            byte[] tokenBytes = jsonObj.getBytes();
            byte[] hmacHash = hmac.doFinal(tokenBytes);

            String base64Token = getBase64String(tokenBytes);
            String base64Hash = getBase64String(hmacHash);

            finalToken = base64Token + TOKEN_SEPERATOR + base64Hash;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return finalToken;
    }

    //endregion

    //region AbstractAuthenticationToken Implementation

    /**
     * Gets the credential represented by this token.
     * @return
     */
    @Override
    public Object getCredentials() {
        return authUser.getPassword();
    }

    /**
     * Gets the principal represented by this token.
     * @return
     */
    @Override
    public Object getPrincipal() {
        return authUser.getLoginId();
    }

    //endregion

    //region Helpers

    /**
     * Converts a byte array to Base 64
     * @param content The bytes.
     * @return The Base 64 string.
     */
    private String getBase64String(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    //endregion
}
