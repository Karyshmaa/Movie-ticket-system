package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.ShowRequestDTO;
import com.kary.moviebooking.dto.ShowResponseDTO;
import com.kary.moviebooking.entity.Screen;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.Movie;
import com.kary.moviebooking.entity.Theater;
import com.kary.moviebooking.repository.ScreenRepository;
import com.kary.moviebooking.repository.ShowRepository;
import com.kary.moviebooking.repository.MovieRepository;
import com.kary.moviebooking.repository.TheaterRepository;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.service.Interface.ShowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowService showService;
    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public ShowController(ShowService showService,ShowRepository showRepository,ScreenRepository screenRepository,MovieRepository movieRepository, TheaterRepository theaterRepository) {
        this.showService = showService;
        this.showRepository = showRepository;
        this.screenRepository = screenRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    // Create a new show
    @PostMapping
    public ShowResponseDTO createShow(@RequestBody ShowRequestDTO requestDTO) {

        // Get Movie
        Movie movie = movieRepository.findById(requestDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + requestDTO.getMovieId()));

        // Get Theater
        Theater theater = theaterRepository.findById(requestDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id " + requestDTO.getTheaterId()));

        // Get Screen
        Screen screen = screenRepository.findById(requestDTO.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id " + requestDTO.getScreenId()));

        // Create new Show
        Show show = new Show();
        show.setMovie(movie);
        show.setTheater(theater);
        show.setScreen(screen);
        show.setShowTime(requestDTO.getShowTime());

        return showService.createShow(show);
    }

    // Get show by ID
    @GetMapping("/{id}")
    public ShowResponseDTO getShowById(@PathVariable Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id " + id));

        return showService.getShowById(id);
    }

    // Get all shows
    @GetMapping
    public List<ShowResponseDTO> getAllShows() {
        return showService.getAllShows();
    }

    // Get shows by Movie ID
    @GetMapping("/movie/{movieId}")
    public List<Show> getShowsByMovie(@PathVariable Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
        return showRepository.findByMovie(movie);
    }

    // Get shows by Theater ID
    @GetMapping("/theater/{theaterId}")
    public List<Show> getShowsByTheater(@PathVariable Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id " + theaterId));
        return showRepository.findByTheater(theater);
    }
}