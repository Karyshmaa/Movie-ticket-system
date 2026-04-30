package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.entity.Seat;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.ShowSeat;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.exception.SeatNotAvailableException;
import com.kary.moviebooking.repository.SeatRepository;
import com.kary.moviebooking.repository.ShowSeatRepository;
import com.kary.moviebooking.service.Interface.ShowSeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowSeatRepository showSeatRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public void createShowSeats(Show show) {

        List<Seat> seats = seatRepository.findAllByScreenId(show.getScreen().getId());

        List<ShowSeat> showSeats = seats.stream().map(seat -> {
            ShowSeat ss = new ShowSeat();
            ss.setShow(show);
            ss.setSeat(seat);
            ss.setSeatStatus(SeatStatus.AVAILABLE);
            ss.setPrice(getDefaultPrice(seat));
            return ss;
        }).collect(Collectors.toList());

        showSeatRepository.saveAll(showSeats);
    }

        @Override
        @Transactional
        public void lockSeats(List<Long> seatIds, Long showId, Long userId) {

            List<ShowSeat> seats = showSeatRepository.findWithLock(showId, seatIds);

            if (seats.size() != seatIds.size()) {
                throw new SeatNotAvailableException("One or more seats not found for this show");
            }

            for (ShowSeat seat : seats) {
                if (seat.getSeatStatus() == SeatStatus.BOOKED) {
                    throw new SeatNotAvailableException(
                            "Seat " + seat.getId() + " is already booked"
                    );
                }
                if (seat.getSeatStatus() == SeatStatus.TEMP_LOCKED) {
                    if (seat.getLockedAt() != null &&
                            seat.getLockedAt().plusMinutes(5).isAfter(LocalDateTime.now())) {
                        throw new SeatNotAvailableException(
                                "Seat " + seat.getId() + " is locked by another user"
                        );
                    }
                }
                // lock the seat
                seat.setSeatStatus(SeatStatus.TEMP_LOCKED);
                seat.setLockedByUserId(userId);
                seat.setLockedAt(LocalDateTime.now());
            }

            showSeatRepository.saveAll(seats);
        }

    // default pricing by seat type
    private Double getDefaultPrice(Seat seat) {
        return switch (seat.getSeatType()) {
            case PREMIUM -> 350.0;
            case RECLINER -> 500.0;
            default -> 250.0;
        };
    }
}