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
}
