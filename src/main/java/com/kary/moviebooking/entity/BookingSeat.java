package com.kary.moviebooking.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
    @Table(
            name = "booking_seats",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"show_id", "seat_id"})
            }
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
        @JoinColumn(name = "show_id")
        private Show show;

        @ManyToOne(optional = false)
        @JoinColumn(name = "seat_id")
        private Seat seat;

        private LocalDateTime bookedAt;;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}


