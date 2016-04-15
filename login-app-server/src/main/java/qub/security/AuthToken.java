package qub.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represents an Auth token that can be given to the client for authentication.
 */
public class AuthToken extends AbstractAuthenticationToken {

    //region Constructor

    /**
     * Constructs the auth token.
     */
    public AuthToken() {
        super(null);

    }

    //region Abstract base implementation.

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    //endregion
}
