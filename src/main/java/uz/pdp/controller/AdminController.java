package uz.pdp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.model.AuthUser;
import uz.pdp.model.Movie;
import uz.pdp.service.movie.MovieService;
import uz.pdp.service.user.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {

    private final MovieService movieService;
    private final UserService userService;

    public AdminController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("page", "ADMIN PAGE");
        return "admin";
    }

    @GetMapping("/admin/addMovies")
    public String addMoviePage(Model model) {
        return "/admin/addMovie";
    }

    @PostMapping("/admin/addMovie")
    public String addMovie(
            @RequestParam("movieTitle") String movieTitle,
            @RequestParam("movieCategory") String movieCategory,
            @RequestParam("movieDescription") String movieDescription,
            @RequestParam("startTime") String startTimeStr,
            @RequestParam("endTime") String endTimeStr,
            @RequestParam("posterImage") MultipartFile posterImage,
            Model model
    ) {
        LocalDate startTime = LocalDate.parse(startTimeStr);
        LocalDate endTime = LocalDate.parse(endTimeStr);

        String posterImagePath = saveFile(posterImage);

        Movie movie = new Movie();
        movie.setName(movieTitle);
        movie.setCategoryName(movieCategory);
        movie.setDescription(movieDescription);
        movie.setReleaseDate(startTime);
        movie.setEndTime(endTime);
        movie.setPosterImage(posterImagePath);
        movie.setCreateDate(LocalDate.now());

        movieService.save(movie);

        model.addAttribute("message", "Movie added successfully!");
        return "redirect:/addMovies";
    }

    private String saveFile(MultipartFile file) {
        return file.getOriginalFilename();
    }
    @GetMapping("/admin/profile")
    public String profilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        AuthUser user = userService.getByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        if ("ADMIN".equals(user.getRole())) {
            return "admin/profile"; // Admin profile page
        } else {
            return "userProfile"; // User profile page
        }
    }


    @GetMapping("/admin/showMovies")
    public String showMoviePage(Model model) {
        return "/admin/showMovies";
    }

    @GetMapping("/admin/showUsers")
    public String showUsersPage(Model model) {
        List<AuthUser> users = userService.getAll();
        model.addAttribute("users", users);
        return "/admin/showUsers";
    }
}
