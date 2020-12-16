package web.controller;

import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.service.UserService;
import web.serviceController.CurrentUser;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private CurrentUser currentUser;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setCurrentUser(CurrentUser currentUser) { this.currentUser = currentUser;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView theUserInformation(Principal principal) {
        return new ModelAndView("templates/user/userList", "user", currentUser.getCurrentUser(principal));
    }
}
