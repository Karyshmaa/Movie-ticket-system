package com.kary.moviebooking.controller;

import com.kary.moviebooking.entity.Booking;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.BookingRepository;
import com.kary.moviebooking.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a new booking
    @PostMapping
    public Booking bookSeats(@RequestParam Long showId,
                             @RequestParam Long userId,
                             @RequestBody List<Long> seatIds) {

        User user = new User();
        user.setId(userId);

        return bookingService.bookSeats(showId, seatIds, user);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
    }

    // Get all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }


    // Delete a booking
    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking deleted successfully!";
    }
}