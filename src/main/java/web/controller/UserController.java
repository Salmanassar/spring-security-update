package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @GetMapping(value = {"infoCall", "/"})
    public ModelAndView index(Principal principal, ModelAndView mav) {
        User user = userService.findUserByEmail(principal.getName()).get();
        mav.addObject("user", user);
        mav.setViewName("templates/user/userList");
        return mav;
    }
}
