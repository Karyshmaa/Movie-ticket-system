package com.kary.moviebooking.entity;

import com.kary.moviebooking.enums.SeatStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public Long getLockedByUserId() {
        return lockedByUserId;
    }

    public void setLockedByUserId(Long lockedByUserId) {
        this.lockedByUserId = lockedByUserId;
    }
}

