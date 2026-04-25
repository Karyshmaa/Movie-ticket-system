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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;   // ✅ only service, no repositories

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowResponseDTO> createShow(
            @RequestBody @Valid ShowRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(showService.createShow(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowResponseDTO> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShowResponseDTO>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowResponseDTO>> getShowsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsByMovieId(movieId));
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ShowResponseDTO>> getShowsByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(showService.getShowsByTheaterId(theaterId));
    }
}