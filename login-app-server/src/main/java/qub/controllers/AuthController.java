package qub.controllers;

import model.AuthResult;
import model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qub.service.IUserService;

public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody AuthResult login(@RequestBody final Credential loginInfo) {

        UsernamePasswordAuthenticationToken loginData = new UsernamePasswordAuthenticationToken(loginInfo.getUserName(), loginInfo.getPassWord());

        if (loginData.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(loginData);
        }

    }

}


