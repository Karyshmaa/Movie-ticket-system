package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.ScreenRequestDTO;
import com.kary.moviebooking.dto.ScreenResponseDTO;
import com.kary.moviebooking.service.Interface.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScreenResponseDTO> createScreen(
            @RequestBody @Valid ScreenRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(screenService.createScreen(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenResponseDTO> getScreenById(@PathVariable Long id) {
        return ResponseEntity.ok(screenService.getScreenById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScreenResponseDTO>> getAllScreens() {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenResponseDTO>> getScreensByTheaterId(
            @PathVariable Long theaterId) {
        return ResponseEntity.ok(screenService.getScreensByTheaterId(theaterId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return ResponseEntity.ok("Screen deleted successfully");
    }
}