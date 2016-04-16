package qub.controllers;

import model.AuthResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qub.domain.user.User;
import qub.service.IAuthenticationService;
import qub.service.ICryptoHashingService;
import qub.service.IUserService;
import util.StringConstants;

import javax.servlet.http.HttpServletResponse;

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
     * The Crypto Service.
     */
    @Autowired
    private ICryptoHashingService cryptoHashingService;

    /**
     * The Auth Manager.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    //endregion

    //region Actions

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody AuthResult login(@RequestBody final Credential loginInfo, HttpServletResponse response) {

        String hash = cryptoHashingService.hashString("Test");

        boolean validCreds = authenticationService.verifyUserCredentials(loginInfo.getUserName(), loginInfo.getPassWord());

        AuthResult returnResult = new AuthResult();

        if (validCreds) {

            User userInfo = userService.getUserByLoginId(loginInfo.getUserName());

            String token = authenticationService.createTokenForUser(userInfo);

            response.setHeader(StringConstants.TOKEN_HEADER_NAME, token);

            returnResult.setSuccess(true);
        }
        else {
            returnResult.setSuccess(false);
            returnResult.setError("Credentials could not be verified.");
        }

        return returnResult;
    }

    //endregion

}


