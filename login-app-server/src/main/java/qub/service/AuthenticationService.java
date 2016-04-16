package qub.service;

import qub.security.AuthToken;
import org.springframework.security.core.Authentication;
import qub.domain.User;

/**
 * Provides an implementation of the IAuthenticationService.
 */
public class AuthenticationService implements IAuthenticationService {

    //region IAuthenticationService Implementation.

    /**
     * Creates an AuthToken for the user and valid auth result.
     * @param userDetails The user details.
     * @return A valid, HMAC signed AuthToken.
     */
    public AuthToken createTokenForUser(User userDetails) {

        AuthToken token = new AuthToken(userDetails);
        return token;
    }

    /**
     * Validates a token and parses an Auth object from it.
     * @param tokenHeader The header.
     * @return The parsed auth object.
     */
    @Override
    public Authentication validateTokenFromUser(String tokenHeader) {
        return null;
    }

    //endregion
}
