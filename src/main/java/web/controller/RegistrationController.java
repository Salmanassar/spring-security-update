package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.UserService;
import web.serviceController.Checkbox;

import javax.validation.Valid;

@Controller
class RegistrationController {

    private UserService userService;
    private Checkbox checkbox;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setCheckbox(Checkbox checkbox) {
        this.checkbox = checkbox;
    }

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("templates/registration", "user", new User());
    }

    @PostMapping(value = "user/register")
    public ModelAndView createUser(@Valid final User user, @RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                                   @RequestParam(name = "isUser", required = false) boolean isUser, final BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("templates/registration", "user", user);
        }
        try {
            checkbox.selectRoleFromCheckbox(user, isAdmin, isUser);
            userService.createUser(user);
        } catch (Exception e) {
            result.addError(new FieldError("user", "user", e.getMessage()));
            return new ModelAndView("templates/registration", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }
}
