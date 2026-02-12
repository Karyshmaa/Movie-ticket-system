package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.Movie;
import com.kary.moviebooking.repository.MovieRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Create a movie
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        movie.setCreatedAt(LocalDateTime.now()); // if your entity has this field
        return movieRepository.save(movie);
    }

    // Get movie by id
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    // Get all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Test endpoint
    @GetMapping("/test-movie")
    public String testMovie() {
        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setDirector("Christopher Nolan");
        movie.setReleaseYear(2014);

        movieRepository.save(movie);

        return "Movie saved!";
    }
}