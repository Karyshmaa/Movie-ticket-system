package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.Movie;
import com.kary.moviebooking.repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMovieController {

    private final MovieRepository movieRepository;

    public TestMovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/test-movie")
    public String testMovie() {
        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setGenre("Sci-Fi");
        movie.setDuration(169);

        movieRepository.save(movie);

        return "Movie saved!";
    }
}