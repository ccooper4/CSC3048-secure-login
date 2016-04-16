package qub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qub.security.AuthToken;
import qub.domain.user.User;
import qub.service.IAuthenticationService;
import qub.service.IHMACSigningService;
import util.StringConstants;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Provides an implementation of the IAuthenticationService.
 */
@Service
public class AuthenticationService implements IAuthenticationService {

    //region Fields

    /**
     * The HMAC Signing Service.
     */
    @Autowired
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

        String finalToken = base64Token + StringConstants.TOKEN_SEPERATOR + base64Sig;

        return finalToken;
    }

    /**
     * Validates a token and parses an Auth object from it.
     * @param tokenHeader The header.
     * @return The parsed auth object.
     */
    @Override
    public AuthToken validateTokenFromUser(String tokenHeader) {

        //Check we actually have a string.
        if (tokenHeader == null || tokenHeader.length() == 0) {
            return null;
        }

        String[] tokenParts = tokenHeader.split("\\" + StringConstants.TOKEN_SEPERATOR);

        //Check we have only 2 parts.
        if (tokenParts == null || tokenParts.length != 2 || tokenParts[0].length() == 0 || tokenParts[1].length() == 0) {
            return null;
        }

        byte[] token = getBytesFromBase64(tokenParts[0]);
        byte[] hmacSig = getBytesFromBase64(tokenParts[1]);

        //Check no one has tampered with the token or signature.
        byte[] reHash = signingService.hmacSign(token);

        if (!Arrays.equals(hmacSig, reHash)) {
            //Something isn't right - log a tampering attempt here.
        }

        //Deserialize the token.

        String tokenText = new String(token);

        AuthToken tokenObj = AuthToken.constructFromJson(tokenText);

        return tokenObj;
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

    /**
     * Gets a byte array from a base 64 string.
     * @param b64 The base 64 string.
     * @return The byte array.
     */
    private byte[] getBytesFromBase64(String b64) {

        return DatatypeConverter.parseBase64Binary(b64);

    }

    //endregion
}
