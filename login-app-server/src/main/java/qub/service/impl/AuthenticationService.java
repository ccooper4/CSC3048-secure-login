package qub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import qub.domain.IssuedToken;
import qub.security.AuthToken;
import qub.domain.user.User;
import qub.service.IAuthenticationService;
import qub.service.ICryptoHashingService;
import qub.service.IUserService;
import util.EncryptedLogger;
import util.StringConstants;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;

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
    private ICryptoHashingService signingService;

    /**
     * The user service.
     */
    @Autowired
    private IUserService userService;

    /**
     * The logger for this class.
     */
    private EncryptedLogger log = new EncryptedLogger(getClass());

    //endregion

    //region IAuthenticationService Implementation.

    /**
     * Creates an Auth Token for the specified user.
     *
     * @param userDetails The user details.
     * @param ipAddress   The IP Address.
     * @param tokenExpiry This tokens expiry date.
     * @return A valid, HMAC Signed auth token that can be handed to the user.
     */
    @Override
    public String createTokenForUser(User userDetails, String ipAddress, Date tokenExpiry) {

        AuthToken token = new AuthToken(userDetails);

        token.setIpAddres(ipAddress);
        token.setExpiryDate(tokenExpiry);
        token.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(token);

        String jsonToken = token.toString();

        byte[] tokenBytes = jsonToken.getBytes();
        byte[] hmacTokenBytesSig = signingService.hmacSign(tokenBytes);

        String base64Token = getBase64String(tokenBytes);
        String base64Sig = getBase64String(hmacTokenBytesSig);

        String finalToken = base64Token + StringConstants.TOKEN_SEPERATOR + base64Sig;

        //Save the token to the user.
        IssuedToken tokenRef = new IssuedToken();
        tokenRef.setTokenId(token.getTokenId());
        tokenRef.setUser(userDetails);


        userDetails.addIssuedToken(tokenRef);
        userService.saveUser(userDetails);

        return finalToken;
    }

    /**
     * Parses the Authentication details from the token passed in the request header.
     *
     * @param tokenHeader The header.
     * @param userIp      The user's IP.
     * @return The parsed authentication object.
     */
    @Override
    public AuthToken validateTokenFromUser(String tokenHeader, String userIp) {

        //Check we actually have a string.
        if (tokenHeader == null || tokenHeader.length() == 0) {

            log.info("Failed whilst verifying token - token is not set");

            return null;
        }

        String[] tokenParts = tokenHeader.split("\\" + StringConstants.TOKEN_SEPERATOR);

        //Check we have only 2 parts.
        if (tokenParts == null || tokenParts.length != 2 || tokenParts[0].length() == 0 || tokenParts[1].length() == 0) {

            log.info("Failed whilst verifying token - token is invalid.");

            return null;
        }

        byte[] token = getBytesFromBase64(tokenParts[0]);
        byte[] hmacSig = getBytesFromBase64(tokenParts[1]);

        //Check no one has tampered with the token or signature.
        byte[] reHash = signingService.hmacSign(token);

        if (!Arrays.equals(hmacSig, reHash)) {
            //Something isn't right - log a tampering attempt here.

            log.info("Token hash did not match whilst verifying token");

            return null;
        }

        //Deserialize the token.

        String tokenText = new String(token);

        AuthToken tokenObj = AuthToken.constructFromJson(tokenText);

        String tokenId = tokenObj.getTokenId();

        Date timeNow = new Date();

        if (tokenObj.getExpiryDate().before(timeNow)) {
            //Log that the token has expired.

            log.info("Token " + tokenId + " has expired.");

            return null;
        }

        if (!tokenObj.getIpAddres().equals(userIp)) {
            //Log that there is an IP mismatch.

            log.info("Token "  + tokenId + " IP did not match client IP.");

            return null;
        }

        User dbUser = userService.getUserByLoginId(tokenObj.getName());

        if (dbUser == null) {

            log.info("User expressed by token "  + tokenId + " was not found in the database.");

            return null;
        }

        if (dbUser.getIssuedTokens().stream().noneMatch(it -> it.getTokenId().equals(tokenObj.getTokenId()))) {

            log.info("Token "  + tokenId + " was not found in the user's active token set.");

            return null;
        }

        return tokenObj;
    }

    /**
     * Verifies the user's credentials.
     *
     * @param userId   The username.
     * @param password The password.
     * @return A boolean indicating if the credentials are valid.
     */
    @Override
    public int verifyUserCredentialsAndLockoutStatus(String userId, String password) {

        log.info("Verifying credentials for user: " + userId);

        User foundUser = userService.getUserByLoginId(userId);

        if (foundUser == null) {

            log.info("Could not find user " + userId);

            return 0;
        }

        String storedHash = foundUser.getPassword();

        if (signingService.verifyStringAgainstHash(password, storedHash)) {
            if (foundUser.getLockedOutUntil() == null || new Date(System.currentTimeMillis()).after(foundUser.getLockedOutUntil())) {
                foundUser.resetLoginAttemptsSinceLastUnsuccessful();
                userService.saveUser(foundUser);
                log.info("User: " + userId + " has logged in succesfully.");
                return 1;
            } else {
                log.info("User: " + userId + " has attempted to login with correct details, but is still timed out.");
                return 2;
            }                
        } else {
            foundUser.incrementLoginAttemptsSinceLastUnsuccessful();
            userService.saveUser(foundUser);
            log.info("User: " + userId + " has attempted to login with incorrect details, count incremented to " + foundUser.getLoginAttemptsSinceLastUnsuccessful());
            return 0;
        }

    }

    //endregion

    //region Helpers

    /**
     * Converts a byte array to Base 64
     *
     * @param content The bytes.
     * @return The Base 64 string.
     */
    private String getBase64String(byte[] content) {

        return DatatypeConverter.printBase64Binary(content);

    }

    /**
     * Gets a byte array from a base 64 string.
     *
     * @param b64 The base 64 string.
     * @return The byte array.
     */
    private byte[] getBytesFromBase64(String b64) {

        return DatatypeConverter.parseBase64Binary(b64);

    }

    //endregion
}
