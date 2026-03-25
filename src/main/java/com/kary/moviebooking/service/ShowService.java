package com.kary.moviebooking.service;

import com.kary.moviebooking.entity.Seat;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.ShowSeat;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.SeatRepository;
import com.kary.moviebooking.repository.ShowRepository;
import com.kary.moviebooking.repository.ShowSeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final ShowSeatService showSeatService;
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;

    public ShowService(ShowRepository showRepository,
                       ShowSeatService showSeatService,
                       SeatRepository seatRepository,
                       ShowSeatRepository showSeatRepository) {
        this.showRepository = showRepository;
        this.showSeatService = showSeatService;
        this.seatRepository = seatRepository;
        this.showSeatRepository = showSeatRepository;
    }

    public Show createShow(Show show) {

        // Step 1: Save the show
        Show savedShow = showRepository.save(show);

        // 2. Get all seats of that screen
        Long screenId = show.getScreen().getId();
        List<Seat> seats = seatRepository.findByScreen_Id(screenId);

        // 3. Create ShowSeat for each seat
        List<ShowSeat> showSeats = new ArrayList<>();

        for (Seat seat : seats) {
            ShowSeat ss = new ShowSeat();
            ss.setShow(savedShow);
            ss.setSeat(seat);
            ss.setSeatStatus(SeatStatus.AVAILABLE);

            showSeats.add(ss);
        }

        // 4. Save all at once
        showSeatRepository.saveAll(showSeats);

        return savedShow;
    }
}