package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.Movie;
import com.kary.moviebooking.entity.Theater;
import com.kary.moviebooking.repository.ShowRepository;
import com.kary.moviebooking.repository.MovieRepository;
import com.kary.moviebooking.repository.TheaterRepository;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public ShowController(ShowRepository showRepository, MovieRepository movieRepository, TheaterRepository theaterRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    // Create a new show
    @PostMapping
    public Show createShow(@RequestBody Show show) {
        // Check if Movie exists
        Movie movie = movieRepository.findById(show.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + show.getMovie().getId()));
        // Check if Theater exists
        Theater theater = theaterRepository.findById(show.getTheater().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id " + show.getTheater().getId()));

        show.setMovie(movie);
        show.setTheater(theater);

        return showRepository.save(show);
    }

    // Get show by ID
    @GetMapping("/{id}")
    public Show getShowById(@PathVariable Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id " + id));
    }

    // Get all shows
    @GetMapping
    public List<Show> getAllShows() {
        return showRepository.findAll();
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