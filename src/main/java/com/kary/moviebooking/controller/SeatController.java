package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.LockSeatsRequestDTO;
import com.kary.moviebooking.service.Impl.ShowSeatServiceImpl;
import com.kary.moviebooking.service.Interface.ShowSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final ShowSeatService showSeatService;

    @PostMapping("/lock")
    public ResponseEntity<String> lockSeats(
            @RequestParam Long showId,
            @RequestParam List<Long> seatIds,
            @AuthenticationPrincipal UserDetails userDetails) {  // ✅ userId from JWT

        // get userId from security context — not from request body
        // you'll need a method to fetch user by email, similar to BookingService
        showSeatService.lockSeats(seatIds, showId, null); // pass real userId after fetching

        return ResponseEntity.ok("Seats locked successfully");
    }
}