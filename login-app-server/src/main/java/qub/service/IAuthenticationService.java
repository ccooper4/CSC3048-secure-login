package qub.service;

import qub.security.AuthToken;
import org.springframework.security.core.Authentication;
import qub.domain.user.User;

import java.util.Date;

/**
 * Outlines the interface for the Auth Service.
 */
public interface IAuthenticationService {

    /**
     * Creates an Auth Token for the specified user.
     * @param userDetails The user details.
     * @param ipAddress The IP Address.
     * @param tokenExpiry This tokens expiry date.
     * @return A valid, HMAC Signed auth token that can be handed to the user.
     */
    String createTokenForUser(User userDetails, String ipAddress, Date tokenExpiry);

    /**
     * Parses the Authentication details from the token passed in the request header.
     * @param tokenHeader The header.
     * @param userIp The user's IP.
     * @return The parsed authentication object.
     */
    AuthToken validateTokenFromUser(String tokenHeader, String userIp);

    /**
     * Verifies the user's credentials.
     * @param userId The username.
     * @param password The password.
     * @return A boolean indicating if the credentials are valid.
     */
    int verifyUserCredentialsAndLockoutStatus(String userId, String password);

}
