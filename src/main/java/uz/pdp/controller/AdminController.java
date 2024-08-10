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
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("page", "ADMIN PAGE");
        return "admin";
    }

    @GetMapping("/admin/addMovies")
    public String addMoviePage() {
        return "/admin/addMovie";
    }

    @PostMapping("/admin/addMovie")
    public String addMovie(
            @RequestParam("movieTitle") String movieTitle,
            @RequestParam("movieCategory") String movieCategory,
            @RequestParam("movieDescription") String movieDescription,
            @RequestParam("releaseDate") String releaseDateStr,
            @RequestParam("duration") int duration,
            @RequestParam("posterImage") MultipartFile posterImage,
            Model model
    ) throws IOException {
        LocalDate releaseDate = LocalDate.parse(releaseDateStr);

        Movie movie = new Movie();
        movie.setName(movieTitle);
        movie.setCategoryName(movieCategory);
        movie.setDescription(movieDescription);
        movie.setReleaseDate(releaseDate);
        movie.setDuration(duration);
        movie.setCreateDate(LocalDate.now());

        String base64Image = Base64.getEncoder().encodeToString(posterImage.getBytes());
        movie.setPosterImage(base64Image);

        movieService.save(movie);

        model.addAttribute("message", "Movie added successfully!");
        return "redirect:/admin/addMovies";
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
        return "redirect:/admin/addTheater";
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
        return "redirect:/admin/addScreen";
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
