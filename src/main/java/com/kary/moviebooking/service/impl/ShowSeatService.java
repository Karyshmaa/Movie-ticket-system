package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.entity.Seat;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.ShowSeat;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.SeatRepository;
import com.kary.moviebooking.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowSeatService {
    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private SeatRepository seatRepository;

    public void createShowSeats(Show show) {

        System.out.println("Creating show seats...");

        List<Seat> seats = seatRepository
                .findAllByScreenId(show.getScreen().getId());

        for (Seat seat : seats) {
            ShowSeat ss = new ShowSeat();
            ss.setShow(show);
            ss.setSeat(seat);
            ss.setSeatStatus(SeatStatus.AVAILABLE);

            showSeatRepository.save(ss);
        }
    }
}
