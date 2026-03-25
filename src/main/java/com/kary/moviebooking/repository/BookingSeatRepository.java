package com.kary.moviebooking.repository;

import com.kary.moviebooking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByShow_IdAndSeat_IdIn(Long showId, List<Long> seatIds);
}