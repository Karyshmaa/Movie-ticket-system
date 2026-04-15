package com.kary.moviebooking.entity;

import com.kary.moviebooking.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ShowSeat {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private Show show;

        @ManyToOne
        private Seat seat;

        @Enumerated(EnumType.STRING)
        private SeatStatus seatStatus;

        private LocalDateTime lockedAt;

        private Long lockedByUserId;

       @ManyToOne
       @JoinColumn(name = "booking_id")
       private Booking booking;
}

