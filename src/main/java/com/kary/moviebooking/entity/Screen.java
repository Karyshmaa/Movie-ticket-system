package com.kary.moviebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "screens",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"theater_id", "name"}
        ))
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;   // Screen 1, Audi 1

    @ManyToOne(optional = false)
    @JoinColumn(name = "theater_id")
    @JsonIgnore
    private Theater theater;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    @JsonIgnore
    private java.util.List<Seat> seats;
}