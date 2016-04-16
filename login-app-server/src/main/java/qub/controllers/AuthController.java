package qub.controllers;

import model.AuthResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import qub.security.AuthToken;
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
     * The Auth Manager.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${secretKey}")
    private byte[] secretKey;

    //endregion

    //region Actions

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody AuthResult login(@RequestBody final Credential loginInfo, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken loginData = new UsernamePasswordAuthenticationToken(loginInfo.getUserName(), loginInfo.getPassWord());

        Authentication res = authenticationManager.authenticate(loginData);

        AuthResult returnResult = new AuthResult();

        if (res.isAuthenticated()) {

            SecurityContextHolder.getContext().setAuthentication(res);

            User userInfo = userService.getUserByLoginId(res.getName());

            AuthToken token = authenticationService.createTokenForUser(userInfo);

            response.setHeader(StringConstants.TOKEN_HEADER_NAME, token.toString());

            returnResult.setSuccess(true);
        }
        else
        {
            returnResult.setSuccess(false);
            returnResult.setError("Credentials could not be verified.");
        }

        return returnResult;
    }

    //endregion

}


