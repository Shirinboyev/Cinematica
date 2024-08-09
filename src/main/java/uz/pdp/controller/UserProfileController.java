package uz.pdp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.daos.userDao.UserDao;
import uz.pdp.model.AuthUser;

@Controller
public class UserProfileController {

    private final UserDao userDao;


    public UserProfileController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/user/profile")
    public String userProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        AuthUser user = userDao.getByUsername(username).orElse(null);

        if (user != null) {
            AuthUser authUser = new AuthUser();
            authUser.setId(user.getId());
            authUser.setFullName(user.getFullName());
            authUser.setUsername(user.getUsername());
            authUser.setEmail(user.getEmail());
            authUser.setRole(user.getRole());
            authUser.setCreateDate(user.getCreateDate());

            model.addAttribute("userProfile", authUser);
        }

        return "userProfile";
    }
}
