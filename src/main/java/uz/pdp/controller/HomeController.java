package uz.pdp.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.model.*;
import uz.pdp.service.movie.MovieService;
import uz.pdp.service.screen.ScreenService;
import uz.pdp.service.showTime.ShowTimeService;
import uz.pdp.service.ticket.TicketService;
import uz.pdp.service.user.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final MovieService movieService;
    private final ShowTimeService showTimeService;
    private final ScreenService screenService;
    private final TicketService ticketService;

    public HomeController(UserService userService, MovieService movieService, ShowTimeService showTimeService, ScreenService screenService, TicketService ticketService) {
        this.userService = userService;
        this.movieService = movieService;
        this.showTimeService = showTimeService;
        this.screenService = screenService;
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String home(Model model) {
        addUserInfoToModel(model);
        addMoviesToModel(model);
        return "homePage";
    }

    @GetMapping("/movies")
    public String moviePage(Model model) {
        addMoviesToModel(model);
        return "movie";
    }

    @GetMapping("/movies/details/{id}")
    public String userMovieDetails(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            List<ShowTime> showTimes = showTimeService.getShowTimeByMovieId(id);
            model.addAttribute("movie", movie);
            model.addAttribute("showTimes", showTimes);
            return "movieDetails";
        }
        model.addAttribute("errorMessage", "Movie not found.");
        return "error";
    }

    @GetMapping("/showtime/{showTimeId}")
    public String viewShowTimeDetails(@PathVariable("showTimeId") int showTimeId, Model model) {
        ShowTime showTime = showTimeService.getById(showTimeId);
        if (showTime != null) {
            model.addAttribute("showTime", showTime);
            Movie movie = movieService.getById(showTime.getMovieId());
            model.addAttribute("movie", movie);
            Screens screen = screenService.getById(showTime.getScreenId());
            model.addAttribute("screen", screen);
            List<Integer> bookedSeats = ticketService.getBookedSeats(showTime.getId());
            model.addAttribute("bookedSeats", bookedSeats);

            return "showTimeDetails";
        }
        model.addAttribute("errorMessage", "Showtime not found.");
        return "error";
    }


    @PostMapping("/buy-ticket")
    public String buyTicket(@RequestParam("seats") String seats,
                            @RequestParam("price") double totalPrice,
                            @RequestParam("showtimeId") int showtimeId,
                            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AuthUser user = userService.getByUsername(userDetails.getUsername()).orElse(null);

        ShowTime showTime = showTimeService.getById(showtimeId);

        if (showTime == null || user == null) {
            model.addAttribute("errorMessage", "ShowTime or User not found.");
            return "errorPage";
        }

        List<Integer> bookedSeats = ticketService.getBookedSeats(showTime.getId());
        model.addAttribute("bookedSeats", bookedSeats);

        String[] seatNumbers = seats.split(",");
        if (seatNumbers.length == 0) {
            model.addAttribute("errorMessage", "No seats selected.");
            return "errorPage";
        }

        double pricePerSeat = totalPrice / seatNumbers.length;
        for (String seatNumber : seatNumbers) {
            try {
                int seatNo = Integer.parseInt(seatNumber.trim());

                Tickets ticket = Tickets.builder()
                        .userId(user.getId())
                        .showtimeId(showTime.getId())
                        .time(Timestamp.from(Instant.now()))
                        .price(pricePerSeat)
                        .seatNumber(seatNo)
                        .build();
                ticketService.save(ticket);
            } catch (NumberFormatException e) {
                model.addAttribute("errorMessage", "Invalid seat number format: " + seatNumber);
                return "errorPage";
            } catch (DataIntegrityViolationException e) {
                model.addAttribute("errorMessage", "Failed to save ticket for seat number: " + seatNumber);
                return "errorPage";
            }
        }

        String movieName = movieService.getMovieNameById(showTime.getMovieId()).orElse("Unknown Movie");
        String cinemaName = screenService.getCinemaNameByScreenId(showTime.getScreenId()).orElse("Unknown Cinema");

        model.addAttribute("fullname", user.getFullName());
        model.addAttribute("movieName", movieName);
        model.addAttribute("cinemaName", cinemaName);
        model.addAttribute("seats", seats);
        model.addAttribute("currentTime", showTime.getShowTime());
        model.addAttribute("price", totalPrice);

        return "buyTicket";
    }








    private void addUserInfoToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userService.getByUsername(userDetails.getUsername()).map(AuthUser::getRole).orElse(null));
        } else {
            model.addAttribute("username", null);
            model.addAttribute("role", null);
        }
    }

    private void addMoviesToModel(Model model) {
        List<Movie> allMovies = movieService.getAll();

        List<Movie> availableMovies = allMovies.stream()
                .filter(movie -> showTimeService.existsByMovieId(movie.getId()))
                .toList();

        List<Movie> unavailableMovies = allMovies.stream()
                .filter(movie -> !showTimeService.existsByMovieId(movie.getId()))
                .toList();

        model.addAttribute("availableMovies", availableMovies);
        model.addAttribute("unavailableMovies", unavailableMovies);
    }

}
