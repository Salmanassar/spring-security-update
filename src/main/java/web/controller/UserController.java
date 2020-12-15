package web.controller;

import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView theUserInformation(Principal principal) {
            User user = userService.findUserByEmail(principal.getName()).get();
        return new ModelAndView("templates/user/userList","user", user);
    }
}
