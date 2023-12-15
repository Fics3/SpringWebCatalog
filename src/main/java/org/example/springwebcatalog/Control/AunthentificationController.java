package org.example.springwebcatalog.Control;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.Role;
import org.example.springwebcatalog.Services.ServiceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AunthentificationController {

    private final UserService userService;

    public AunthentificationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("custom-user", new CustomUser());
        addTemplate("/user/registration", "Registration", model);
        return "interface";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("custom-user") CustomUser user, Role role) {
        userService.registerUser(user, role);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        userService.logout(httpServletRequest);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        addTemplate("/user/login", "Login", model);
        return "interface";
    }

    private void addTemplate(String contentTemplate, String title, Model model) {
        model.addAttribute("contentTemplate", contentTemplate);
        model.addAttribute("loggedIn", false);
        model.addAttribute("pageTitle", title);
        model.addAttribute("username", "login");
    }

}
