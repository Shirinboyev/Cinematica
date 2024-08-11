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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.daos.screenDao.ScreenDao;
import uz.pdp.daos.theaterDao.TheaterDao;
import uz.pdp.model.AuthUser;
import uz.pdp.model.Movie;
import uz.pdp.model.Screens;
import uz.pdp.model.Theater;
import uz.pdp.service.movie.MovieService;
import uz.pdp.service.room.RoomService;
import uz.pdp.service.screen.ScreenService;
import uz.pdp.service.user.UserService;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final MovieService movieService;
    private final UserService userService;
    private final RoomService roomService;
    private final ScreenDao screenDao;
    private final ScreenService screenService;

    public AdminController(MovieService movieService, UserService userService, TheaterDao roomDao, RoomService roomService, ScreenDao screenDao, ScreenService screenService) {
        this.movieService = movieService;
        this.userService = userService;
        this.roomService = roomService;
        this.screenDao = screenDao;
        this.screenService = screenService;
    }



    @GetMapping("/admin/moviesDetails.html")
    public String getMovieDescription(@RequestParam("id") int id, Model model) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            model.addAttribute("movie", movie);
            return "/admin/moviesDetails";
        } else {
            return "error";
        }
    }


    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("page", "ADMIN PAGE");
        return "admin";
    }

    @GetMapping("/admin/addMovie")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/addMovie";
    }

    @PostMapping("/admin/addMovie")
    public String addMovie(@RequestParam("movieTitle") String title,
                           @RequestParam("description") String description,
                           @RequestParam("trailerUrl") String trailerUrl,
                           @RequestParam("categoryName") String categoryName,
                           @RequestParam("releaseDate") LocalDate releaseDate,
                           @RequestParam("duration") int duration,
                           @RequestParam("posterImage") MultipartFile posterImage,
                           RedirectAttributes redirectAttributes) {

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setTrailer_url(trailerUrl);
        movie.setCategoryName(categoryName);
        movie.setReleaseDate(releaseDate);
        movie.setDuration(duration);

        try {
            movieService.addMovie(movie, posterImage);
            redirectAttributes.addFlashAttribute("message", "Movie successfully added!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload movie poster!");
        }

        return "redirect:/admin";
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
            return "admin/profile";
        } else {
            return "userProfile";
        }
    }


    @GetMapping("/admin/showMovies")
    public String showMoviesPage(Model model) {
        List<Movie> moviesList = movieService.getAll();
        model.addAttribute("movies", moviesList);
        return "admin/showMovies";
    }


    @GetMapping("/admin/showUsers")
    public String showUsersPage(Model model) {
        List<AuthUser> users = userService.findByRole("USER");
        model.addAttribute("users", users);
        return "/admin/showUsers";
    }
    @GetMapping("/admin/addTheater")
    public String addTheaterPage() {
        return "/admin/addTheater";
    }
    @PostMapping("/admin/addTheater")
    public String addTheater(@RequestParam("name") String name, @RequestParam("location") String location, Model model) {
        Theater rooms = new Theater();
        rooms.setName(name);
        rooms.setLocation(location);
        roomService.save(rooms);
        model.addAttribute("message", "Theater added successfully!");
        return "redirect:/admin";
    }
    @GetMapping("/admin/addScreen")
    public String showAddScreenForm(Model model) {
        List<Theater> theaters = roomService.getAll();
        Map<Integer, String> theaterMap = theaters.stream()
                .collect(Collectors.toMap(Theater::getId, Theater::getName));
        model.addAttribute("theaterMap", theaterMap);
        return "/admin/addScreens";
    }

    @PostMapping("/admin/addScreen")
    public String addEntry(@RequestParam("name") String name,
                           @RequestParam("theaterId") int theaterId,
                           @RequestParam("capacity") int capacity,
                           Model model) {
        Screens screens = new Screens();
        screens.setName(name);
        screens.setRoomId(theaterId);
        screens.setCapacity(capacity);
        screenDao.save(screens);
        model.addAttribute("message", "Screen added successfully!");
        return "redirect:/admin";
    }
    @GetMapping("/admin/showScreens")
    public String screens(Model model) {
        List<Screens> screens = screenService.getAll();
        List<Theater> theaters = roomService.getAll();
        Map<Integer, String> theaterMap = theaters.stream()
                .collect(Collectors.toMap(Theater::getId, Theater::getName));
        model.addAttribute("screens", screens);
        model.addAttribute("theaterMap", theaterMap);
        return "/admin/showScreens";
    }

    @GetMapping("/admin/showTheaters")
    public String showTheatersPage(Model model) {
        List<Theater> theatersList = roomService.getAll();
        model.addAttribute("theaters", theatersList);
        return "/admin/showTheater";
    }
}