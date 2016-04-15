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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

        User user = userService.getUsersByFirstName(name).get(0);

        return gson.toJson(user);
    }

    @RequestMapping(value="/signOut", method = RequestMethod.GET)
    public void logout (HttpServletRequest request) throws ServletException {

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }
}
