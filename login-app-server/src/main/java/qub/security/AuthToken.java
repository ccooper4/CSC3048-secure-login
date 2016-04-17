package qub.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import qub.domain.user.User;
import qub.util.GsonWrapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * Represents the Auth Token used for authentication.
 */
public class AuthToken extends AbstractAuthenticationToken {

    //region Fields

    /**
     * Gets the user this token represents.
     */
    private User authUser;

    /**
     * Gets the IP Address this token was assigned to.
     */
    private String ipAddres;

    /**
     * Gets the token's expiry date.
     */
    private Date expiryDate;

    /**
     * Gets a unique id for this token.
     */
    private String tokenId;

    //endregion

    //region Constructor

    /**
     * Constructs a new Auth Token.
     */
    public AuthToken(User authUser) {
        super(null);
        this.authUser = authUser;

        this.setAuthenticated(true);
        tokenId = UUID.randomUUID().toString();
    }

    /**
     * Constructs a new Auth Token with no user.
     */
    public AuthToken() {
        super(null);
        tokenId = UUID.randomUUID().toString();
    }

    //endregion

    //region Getters & Setters

    /**
     * Gets the Authenticated user.
     * @return The user.
     */
    public User getAuthUser() {
        return authUser;
    }

    /**
     * Sets the authenticated user.
     * @param authUser The user.
     */
    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    /**
     * Gets the IP Address
     * @return The IP Address.
     */
    public String getIpAddres() {
        return ipAddres;
    }

    /**
     * Sets the IP Address.
     * @param ipAddres The new IP Address.
     */
    public void setIpAddres(String ipAddres) {
        this.ipAddres = ipAddres;
    }

    /**
     * Gets the expiry date.
     * @return The token expiry date.
     */
    public Date getExpiryDate() {
        return expiryDate;
    }


    /**
     * Sets the expiry date.
     * @param expiryDate The token expiry date.
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Gets the token id.
     * @return The UUID.
     */
    public String getTokenId() {
        return tokenId;
    }

    //endregion

    //region ToString Override

    /**
     * Returns this token as a HMAC Signed, Base-64 encoded token.
     * @return
     */
    @Override
    public String toString() {

        Gson gson = GsonWrapper.getGson();

        String jsonObj = gson.toJson(this);

        return jsonObj;
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

    //region Methods

    /**
     * Constructs an auth token from a JSON String.
     * @param json The JSON.
     * @return The AuthToken.
     */
    public static AuthToken constructFromJson(String json) {

        Gson gson = GsonWrapper.getGson();

        AuthToken newToken = gson.fromJson(json, AuthToken.class);

        return newToken;
    }

    //endregion
}
