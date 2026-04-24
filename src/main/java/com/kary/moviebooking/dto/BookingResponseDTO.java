package com.kary.moviebooking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponseDTO {
    private Long bookingId;
    private Long showId;
    private Long userId;
    private Double totalAmount;
    private String status;
    private String razorpayOrderId;        // ✅ frontend needs this to open Razorpay checkout
    private List<Long> lockedSeatIds;
    private LocalDateTime bookedAt;
}

