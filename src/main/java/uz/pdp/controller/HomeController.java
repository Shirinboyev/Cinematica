package uz.pdp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.model.AuthUser;
import uz.pdp.service.user.UserService;

@Controller
public class HomeController {
    private final UserService userService;
    private  AuthUser authUser;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userService.getByUsername(userDetails.getUsername()).map(AuthUser::getRole).orElse(null));
        } else {
            model.addAttribute("username", null);
            model.addAttribute("role", null);
        }
        return "homePage";
    }


    @GetMapping("/movies")
    public String moviePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "movie";
    }

}
