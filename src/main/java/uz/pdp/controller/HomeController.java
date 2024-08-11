package uz.pdp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.model.AuthUser;
import uz.pdp.model.Movie;
import uz.pdp.service.movie.MovieService;
import uz.pdp.service.user.UserService;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final MovieService movieService;
    private  AuthUser authUser;

    public HomeController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
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
    @GetMapping("/home")
    public String home1(Model model) {
        List<Movie> movies = movieService.getAll();
        model.addAttribute("movies", movies);
        return "homePage";
    }


    @GetMapping("/movies")
    public String moviePage(Model model) {
        model.addAttribute("movies", movieService.getAll());
        return "movie";
    }



    @GetMapping("/movies/details/{id}")
    public String userMovieDetails1(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            model.addAttribute("movie", movie);
            return "movieDetails";
        } else {
            return "error";
        }
    }



}
