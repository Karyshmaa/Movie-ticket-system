package com.kary.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponseDTO {
    private Long showId;
    private Long userId;
    private Long bookingId;
    private LocalDateTime bookedAt;
    private List<Long> seatIds;

}

