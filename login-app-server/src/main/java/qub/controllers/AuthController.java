package qub.controllers;

import model.AuthResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qub.domain.IssuedToken;
import qub.domain.user.User;
import qub.security.AuthToken;
import qub.service.IAuthenticationService;
import qub.service.IUserService;
import util.EncryptedLogger;
import util.StringConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class AuthController {

    //region Dependencies

    /**
     * The user service.
     */
    @Autowired
    private IUserService userService;

    /**
     * The Auth Service.
     */
    @Autowired
    private IAuthenticationService authenticationService;

    /**
     * The logger for this class.
     */
    private EncryptedLogger log = new EncryptedLogger(AuthController.class);

    //endregion

    //region Actions

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody AuthResult login(@RequestBody final Credential loginInfo, HttpServletResponse response, HttpServletRequest request) {

        log.info("New login request for user: " + loginInfo.getUserName());

        boolean validCreds = authenticationService.verifyUserCredentials(loginInfo.getUserName(), loginInfo.getPassWord());

        AuthResult returnResult = new AuthResult();

        if (validCreds) {

            User userInfo = userService.getUserByLoginId(loginInfo.getUserName());

            String userIp = request.getRemoteAddr();

            Date currentDate = new Date();

            Date tokenExpiryDate = new Date(currentDate.getTime() + 3600000);

            String token = authenticationService.createTokenForUser(userInfo, userIp, tokenExpiryDate);

            response.setHeader(StringConstants.TOKEN_HEADER_NAME, token);

            log.info("Login passed for user: " + loginInfo.getUserName() + ", issuing token");

            returnResult.setSuccess(true);
        }
        else {
            log.info("Login failed for user: " + loginInfo.getUserName());
            returnResult.setSuccess(false);
            returnResult.setError("Credentials could not be verified.");
        }

        return returnResult;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public @ResponseBody Boolean logout() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AuthToken token = (AuthToken) auth;
            String tokenId = token.getTokenId();

            User user = userService.getUserByLoginId(auth.getName());
            IssuedToken tokenToDelete = user.removeIssuedToken(tokenId);
            userService.saveUser(user);
            userService.deleteTokenFromRepository(tokenToDelete);

            return true;
        } catch (Exception e) {
            log.error("Error logging out", e);
            return false;
        }
    }



    //endregion

}


