package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.TheaterRequestDTO;
import com.kary.moviebooking.dto.TheaterResponseDTO;
import com.kary.moviebooking.service.Interface.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterResponseDTO> createTheater(
            @RequestBody @Valid TheaterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theaterService.createTheater(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponseDTO> getTheaterById(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }

    @GetMapping
    public ResponseEntity<List<TheaterResponseDTO>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok("Theater deleted successfully");
    }
}