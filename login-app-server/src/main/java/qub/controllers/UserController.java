package qub.controllers;

import model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qub.domain.user.User;
import qub.service.IUserService;
import util.EncryptedLogger;

/**
 * The controller for handling users.
 */
@Controller
public class UserController {

    private EncryptedLogger log = new EncryptedLogger(UserController.class);
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public @ResponseBody UserInfo getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        log.info("Getting current user");
        User user = userService.getUserByLoginId(name);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());

        return userInfo;
    }
}
