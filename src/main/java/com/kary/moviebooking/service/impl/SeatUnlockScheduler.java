package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.entity.ShowSeat;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatUnlockScheduler {

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Scheduled(fixedRate = 30000) //every 1 min
    public void unlockExpiredSeats(){

        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);

        List<ShowSeat> expiredSeats = showSeatRepository.findExpiredLocks(expiryTime);

        for (ShowSeat seat : expiredSeats) {
            seat.setSeatStatus(SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
            seat.setLockedByUserId(null);
        }

        showSeatRepository.saveAll(expiredSeats);

    }
}
