package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.Theater;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.TheaterRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theaters")
public class TheaterController {
    private final TheaterRepository theaterRepository;

    public TheaterController(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    // Create a theater
    @PostMapping
    public Theater createTheater(@RequestBody Theater theater) {
        return theaterRepository.save(theater);
    }

    // Get theater by ID
    @GetMapping("/{id}")
    public Theater getTheaterById(@PathVariable Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id " + id));
    }

    // Get all theaters
    @GetMapping
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    // Optional test endpoint
    @GetMapping("/test-theater")
    public String testTheater() {
        Theater theater = new Theater();
        theater.setName("Galaxy Cinema");
        theater.setLocation("Downtown");

        theaterRepository.save(theater);

        return "Theater saved!";
    }

}
