package com.kary.moviebooking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.enums.SeatType;
import jakarta.persistence.*;

@Entity
@Table(name = "seats",
      uniqueConstraints = @UniqueConstraint(
        columnNames = {"screen_id", "rowNumber", "seatNumber"}
        ))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_row")
    private String rowNumber;   // A, B, C

    @Column(nullable = false)
    private int seatNumber;     // 1, 2, 3

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;  // REGULAR, PREMIUM, RECLINER

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    @JsonIgnore
    private Screen screen;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRowNumber() {
        return rowNumber;
    }
    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }
    public int getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
    public SeatType getSeatType() {
        return seatType;
    }
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
    public Screen getScreen() {
        return screen;
    }
    public void setScreen(Screen screen) {
        this.screen = screen;
    }


}
