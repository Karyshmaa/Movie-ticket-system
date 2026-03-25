package com.kary.moviebooking.service;

import com.kary.moviebooking.entity.Seat;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.ShowSeat;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.SeatRepository;
import com.kary.moviebooking.repository.ShowSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ShowSeatService {

    //constructor injection
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;

    public ShowSeatService(SeatRepository seatRepository,
                               ShowSeatRepository showSeatRepository) {
            this.seatRepository = seatRepository;
            this.showSeatRepository = showSeatRepository;
            
    }

    @Transactional //either all seats get created or none
    public void createShowSeats(Show show) { //This method runs when a new show is created

        List<Seat> seats = seatRepository.findByScreen_Id(show.getScreen().getId());

        List<ShowSeat> showSeats = new ArrayList<>();

        for (Seat seat : seats) {

            ShowSeat showSeat = new ShowSeat();
            showSeat.setShow(show);
            showSeat.setSeat(seat);
            showSeat.setSeatStatus(SeatStatus.AVAILABLE);

            showSeats.add(showSeat);
        }


        showSeatRepository.saveAll(showSeats);
    }
}
