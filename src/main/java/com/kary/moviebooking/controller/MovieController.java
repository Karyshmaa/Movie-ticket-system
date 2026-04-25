package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.MovieRequestDTO;
import com.kary.moviebooking.dto.MovieResponseDTO;
import com.kary.moviebooking.service.Interface.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponseDTO> createMovie(
            @RequestBody @Valid MovieRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieService.createMovie(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")          // ✅ only admin can update
    public ResponseEntity<MovieResponseDTO> updateMovie(
            @PathVariable Long id,
            @RequestBody @Valid MovieRequestDTO request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")          // ✅ only admin can delete
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}
