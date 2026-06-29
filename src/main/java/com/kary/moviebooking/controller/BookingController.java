package com.kary.moviebooking.controller;

import jakarta.validation.Valid;
import com.kary.moviebooking.dto.BookingRequestDTO;
import com.kary.moviebooking.dto.BookingResponseDTO;
import com.kary.moviebooking.entity.Booking;
import com.kary.moviebooking.entity.User;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.service.Impl.BookingServiceImpl;
import com.kary.moviebooking.service.Interface.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;


    @PostMapping("/initiate")
    public ResponseEntity<BookingResponseDTO> initiateBooking(
            @RequestBody @Valid BookingRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {  // ✅ userId from JWT, not body

        BookingResponseDTO response = bookingService.initiateBooking(
                request,
                userDetails.getUsername()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ logged-in user's own bookings ("my bookings" page)
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getBookingsForUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getBookingByIdForUser(id, userDetails.getUsername()));
    }

    // ✅ admin only — view every booking on the platform
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // ✅ user can cancel their own booking; admin can cancel any
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.cancelBookingForUser(id, userDetails.getUsername());
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}
