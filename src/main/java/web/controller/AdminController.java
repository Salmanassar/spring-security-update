package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.User;
import web.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @GetMapping
    @ResponseBody
    public ModelAndView getListUser(Principal principal, ModelAndView modelAndView) {
        modelAndView = new ModelAndView("templates/tl/list");
        modelAndView.addObject("users", this.userService.listUsers());
        modelAndView.addObject("switch", getCurrentAdmin(principal));
        return modelAndView;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String getPageForCreating(@ModelAttribute User user) {
        return "templates/tl/form";
    }

    @RequestMapping("{id}")
    public ModelAndView view(@PathVariable("id") User user) {
        return new ModelAndView("templates/tl/list");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid User user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("templates/tl/form", "formErrors", result.getAllErrors());
        }
        try {
            userService.createUser(user);
        } catch (RuntimeException ex) {
            result.addError(new FieldError("user", "user", ex.getMessage()));
            return new ModelAndView("templates/tl/form", "user", user);
        }
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/admin");
    }

    @RequestMapping(path = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return new ModelAndView("templates/tl/list");
    }

    @RequestMapping(path = {"/edit/{id}"})
    public ModelAndView modifyForm(Model model, @PathVariable("id") Optional<Long> id, User user) {
        if (id.isPresent()) {
            user = userService.getUserById(id.get());
            return new ModelAndView("templates/tl/formUpdate", "user", user);
        }
        return new ModelAndView("redirect:/admin");
    }

    @RequestMapping(path = "/theAdmin",method = RequestMethod.GET)
    public ModelAndView theAdminInformation(Principal principal) {
        return new ModelAndView("templates/tl/currentAdmin","user", getCurrentAdmin(principal));
    }

    private User getCurrentAdmin(Principal principal){
        return userService.findUserByEmail(principal.getName()).get();
    }
}

