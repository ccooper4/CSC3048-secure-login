package qub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import qub.security.AuthToken;
import org.springframework.security.core.Authentication;
import qub.domain.user.User;

import javax.xml.bind.DatatypeConverter;

/**
 * Provides an implementation of the IAuthenticationService.
 */
@Service
public class AuthenticationService implements IAuthenticationService {

    //region Fields

    /**
     * The character used to seperate the token.
     */
    private static final String TOKEN_SEPERATOR = "-";

    /**
     * The HMAC Signing Service.
     */
    private IHMACSigningService signingService;

    //endregion

    //region IAuthenticationService Implementation.

    /**
     * Creates an AuthToken for the user and valid auth result.
     * @param userDetails The user details.
     * @return A valid, HMAC signed AuthToken.
     */
    public String createTokenForUser(User userDetails) {

        AuthToken token = new AuthToken(userDetails);

        String jsonToken = token.toString();

        byte[] tokenBytes = jsonToken.getBytes();
        byte[] hmacTokenBytesSig = signingService.hmacSign(tokenBytes);

        String base64Token = getBase64String(tokenBytes);
        String base64Sig = getBase64String(hmacTokenBytesSig);

        String finalToken = base64Token + TOKEN_SEPERATOR + base64Sig;

        return finalToken;
    }

    /**
     * Validates a token and parses an Auth object from it.
     * @param tokenHeader The header.
     * @return The parsed auth object.
     */
    @Override
    public AuthToken validateTokenFromUser(String tokenHeader) {
        return null;
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
