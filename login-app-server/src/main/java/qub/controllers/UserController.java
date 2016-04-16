package qub.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import qub.domain.user.User;
import qub.service.IUserService;

/**
 * The controller for handling users.
 */
@Controller
public class UserController {

    private Gson gson = new Gson();

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public String getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        User user = userService.getUserByLoginId(name);

        return gson.toJson(user);
    }
}