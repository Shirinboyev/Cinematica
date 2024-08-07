package uz.pdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.model.AuthUser;
import uz.pdp.service.user.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   Model model) {
        AuthUser user = userService.getByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }


    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam("fullName") String fullName,
                               @RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model) {
        if (userService.getByUsername(email) != null) {
            model.addAttribute("error", "This username is already in use. Please use a different email.");
            return "signup";
        }
        AuthUser user = new AuthUser();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userService.save(user);
        return "redirect:/login";
    }


}
