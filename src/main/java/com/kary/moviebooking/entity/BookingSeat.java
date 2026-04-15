package com.kary.moviebooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
    @Table(
            name = "booking_seats",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"show_id", "seat_id"})
            } //to prevent double booking
    )
    public class BookingSeat {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        //Many seats belong to one booking
        @ManyToOne(optional = false)
        @JoinColumn(name = "booking_id")
        private Booking booking;

        @ManyToOne(optional = false)
        @JoinColumn(name = "show_seat_id")
        private ShowSeat showSeat;

        private LocalDateTime bookedAt;;

}


