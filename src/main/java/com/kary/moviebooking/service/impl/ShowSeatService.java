package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.LockSeatsRequestDTO;
import com.kary.moviebooking.entity.Seat;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.ShowSeat;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.SeatRepository;
import com.kary.moviebooking.repository.ShowSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        @Transactional
        public void lockSeats(LockSeatsRequestDTO request) {

            List<ShowSeat> seats = showSeatRepository.findWithLock(
                    request.getShowId(),
                    request.getSeatIds()
            );

            for (ShowSeat seat : seats) {

                if (seat.getSeatStatus() == SeatStatus.BOOKED) {
                    throw new RuntimeException("Seat already booked");
                }

                if(seat.getSeatStatus() == SeatStatus.LOCKED){

                    if(seat.getLockedAt() != null &&
                    seat.getLockedAt().plusMinutes(5).isAfter(LocalDateTime.now())){ //has 5 mins not passed yet
                        throw new RuntimeException("Seat is currently locked by another user");
                    }
                }
                // lock seat
                seat.setSeatStatus(SeatStatus.LOCKED);
                seat.setLockedByUserId(request.getUserId());
                seat.setLockedAt(LocalDateTime.now());
            }

            showSeatRepository.saveAll(seats);
        }
}
