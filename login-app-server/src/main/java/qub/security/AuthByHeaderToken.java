package qub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;
import qub.service.IAuthenticationService;
import util.EncryptedLogger;
import util.StringConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Provides a filter that will initiate the token verification process on each request.
 */
@Service
public class AuthByHeaderToken extends GenericFilterBean {

    //region Fields

    /**
     * The authentication service.
     */
    @Autowired
    private IAuthenticationService authenticationService;

    /**
     * The logger for this class.
     */
    private EncryptedLogger log = new EncryptedLogger(getClass());

    //endregion

    //region GenericFilterBean Implementation

    /**
     * Implements a method that will check for a token in the HTTP Request.
     * @param servletRequest The request.
     * @param servletResponse The response.
     * @param filterChain The filter chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;

        if (httpRequest != null) {

            String token = httpRequest.getHeader(StringConstants.TOKEN_HEADER_NAME);
            if (token != null) {

                log.info("Found " + StringConstants.TOKEN_HEADER_NAME + " in Request. Verifying and setting Auth");

                String userIp = httpRequest.getRemoteAddr();

                AuthToken parsedToken = authenticationService.validateTokenFromUser(token, userIp);

                SecurityContextHolder.getContext().setAuthentication(parsedToken);

            }

        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    //endregion
}
