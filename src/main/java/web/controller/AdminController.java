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
import java.util.List;
import java.util.Optional;

@Controller
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    @GetMapping("/admin")
    @ResponseBody
    public ModelAndView getListUser(Model model) {
        List<User> users = this.userService.listUsers();
        return new ModelAndView("templates/tl/list", "users", users);
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String getPageForCreating(@ModelAttribute User user) {
        return "templates/tl/form";
    }

    @RequestMapping("{id}")
    public ModelAndView view(@PathVariable("id") User user) {

        return new ModelAndView("templates/tl/list");
    }

    //    @PreAuthorize("hasAuthority('users:write')")
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

    //    @PreAuthorize("hasAuthority('users:write')")
    @RequestMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return new ModelAndView("templates/tl/list");
    }

    //    @PreAuthorize("hasAuthority('users:write')")
    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editUser(Model model, @PathVariable("id") Optional<Long> id, boolean isAdmin, boolean isUser) {
        if (id.isPresent()) {
            User user = userService.readUser(id.get());
            userService.updateUser(user);
        }
        return "templates/tl/updateForm";
    }
}

