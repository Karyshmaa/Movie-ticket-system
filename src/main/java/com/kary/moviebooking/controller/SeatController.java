package com.kary.moviebooking.controller;

import com.kary.moviebooking.dto.LockSeatsRequestDTO;
import com.kary.moviebooking.service.Impl.ShowSeatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final ShowSeatService showSeatService;

    public SeatController(ShowSeatService showSeatService){
        this.showSeatService = showSeatService;
    }

    @PostMapping("/lock")
    public String lockSeats(@RequestBody LockSeatsRequestDTO request){
        showSeatService.lockSeats(request);
        return "Seats Locked Successfully";
    }
}
