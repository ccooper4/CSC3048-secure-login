package qub.controllers;

import model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qub.domain.user.User;
import qub.service.IUserService;

/**
 * Controller to handle registration requests.
 */
@Controller
public class RegisterController {

    //region Private fields

    @Autowired
    private IUserService userService;

    //endregion

    //region Public methods

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody String register(@RequestBody final UserInfo userInfo) {

        User newUser = userService.createStandardUser(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getPassword());

        return newUser.getLoginId();
    }

    //endregion
}
