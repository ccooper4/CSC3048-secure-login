package qub.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import qub.service.IUserService;

/**
 * The controller for handling users.
 */
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Gson gson = new Gson();

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public String getCurrentUser(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        return gson.toJson(userService.getUserByName(name));
    }
}
