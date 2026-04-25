package com.kary.moviebooking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.kary.moviebooking.entity.Theater;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Column(nullable = false)
    private LocalDateTime showTime;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
}