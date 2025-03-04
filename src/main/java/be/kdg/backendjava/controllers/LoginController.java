package be.kdg.backendjava.controllers;

import be.kdg.backendjava.domain.User;
import be.kdg.backendjava.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class LoginController {

    private final UserService userService;


    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @PostMapping("/login")
    String signInUser() {
        return "redirect:/user";
    }

    @GetMapping("/register")
    String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    String register(User user) {
        userService.signUpUser(user);
        return "redirect:/login";
    }
}
