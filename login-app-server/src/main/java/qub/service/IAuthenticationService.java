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
    AuthToken createTokenForUser(User userDetails);

    /**
     * Parses the Authentication details from the token passed in the request header.
     * @param tokenHeader The header.
     * @return The parsed authentication object.
     */
    Authentication validateTokenFromUser(String tokenHeader);

}
