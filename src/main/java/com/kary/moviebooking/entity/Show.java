package com.kary.moviebooking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.kary.moviebooking.entity.Theater;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne(optional = false)
    private Screen screen;

    @Column(nullable = false)
    private LocalDateTime showTime;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public Screen getScreen() { return screen; }
    public void setScreen(Screen screen) { this.screen = screen; }

    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }

    public Theater getTheater() {
        return theater;
    }
    public void setTheater(Theater theater) {
        this.theater = theater;
    }
}