package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.Screen;
import com.kary.moviebooking.repository.ScreenRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screens")
public class ScreenController {
    private final ScreenRepository screenRepository;

    public ScreenController(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    // Create a new screen
    @PostMapping
    public Screen createScreen(@RequestBody Screen screen) {
        return screenRepository.save(screen);
    }

    // Get screen by ID
    @GetMapping("/{id}")
    public Screen getScreenById(@PathVariable Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screen not found with id " + id));
    }

    // Get all screens
    @GetMapping
    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    // Get all screens by theater ID
    @GetMapping("/theater/{theaterId}")
    public List<Screen> getScreensByTheaterId(@PathVariable Long theaterId) {
        return screenRepository.findByTheaterId(theaterId);
    }

    // Delete a screen
    @DeleteMapping("/{id}")
    public String deleteScreen(@PathVariable Long id) {
        screenRepository.deleteById(id);
        return "Screen deleted!";
    }
}
