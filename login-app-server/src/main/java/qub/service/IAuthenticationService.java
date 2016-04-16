package qub.service;

import qub.security.AuthToken;
import org.springframework.security.core.Authentication;
import qub.domain.user.User;

/**
 * Outlines the interface for the Auth Service.
 */
public interface IAuthenticationService {

    /**
     * Creates an Auth Token for the specified user.
     * @param userDetails The user details.
     * @return A valid, HMAC Signed auth token that can be handed to the user.
     */
    String createTokenForUser(User userDetails);

    /**
     * Parses the Authentication details from the token passed in the request header.
     * @param tokenHeader The header.
     * @return The parsed authentication object.
     */
    AuthToken validateTokenFromUser(String tokenHeader);

    /**
     * Verifies the user's credentials.
     * @param userId The username.
     * @param password The password.
     * @return A boolean indicating if the credentials are valid.
     */
    boolean verifyUserCredentials(String userId, String password);

}
