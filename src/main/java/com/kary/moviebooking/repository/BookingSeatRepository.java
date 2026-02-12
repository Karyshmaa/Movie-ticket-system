package com.kary.moviebooking.repository;

import com.kary.moviebooking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    // Check if a seat is already booked for a show
    boolean existsByShowIdAndSeatId(Long showId, Long seatId);

    // Get all booked seats for a show
    List<BookingSeat> findByShowId(Long showId);
}