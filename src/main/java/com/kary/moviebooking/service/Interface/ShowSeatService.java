package com.kary.moviebooking.service.Interface;

import com.kary.moviebooking.entity.Show;

import java.util.List;

public interface ShowSeatService {
    void createShowSeats(Show show);
    void lockSeats(List<Long> seatIds, Long showId, Long userId);
}
