package com.kary.moviebooking.controller;

import jakarta.validation.Valid;
import com.kary.moviebooking.dto.BookingRequestDTO;
import com.kary.moviebooking.dto.BookingResponseDTO;
import com.kary.moviebooking.entity.Booking;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.service.Impl.BookingServiceImpl;
import com.kary.moviebooking.service.Interface.BookingService;
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
    public BookingResponseDTO bookSeats(@Valid @RequestBody BookingRequestDTO request) {

        Booking booking = bookingService.createBooking(
                request.getUserId(),
                request.getShowId()
        );

        // convert to response DTO
        BookingResponseDTO response = new BookingResponseDTO();
        response.setBookingId(booking.getId());
        response.setShowId(booking.getShow().getId());
        response.setUserId(booking.getUser().getId());
        response.setBookedAt(booking.getBookedAt());

        return response;
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