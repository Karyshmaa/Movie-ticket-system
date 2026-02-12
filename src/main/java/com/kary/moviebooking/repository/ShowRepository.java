package com.kary.moviebooking.repository;

import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.Movie;
import com.kary.moviebooking.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    // Find all shows for a specific movie
    List<Show> findByMovie(Movie movie);

    // Find all shows for a specific theater
    List<Show> findByTheater(Theater theater);
}