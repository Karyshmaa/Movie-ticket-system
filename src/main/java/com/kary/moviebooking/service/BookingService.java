package com.kary.moviebooking.service;

import com.kary.moviebooking.entity.*;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.*;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
    @Transactional
    public class BookingService {

        private final BookingRepository bookingRepository;
        private final BookingSeatRepository bookingSeatRepository;
        private final SeatRepository seatRepository;
        private final ShowRepository showRepository;
        private final ShowSeatRepository showSeatRepository;

        public BookingService(BookingRepository bookingRepository, BookingSeatRepository bookingSeatRepository, SeatRepository seatRepository, ShowRepository showRepository, ShowSeatRepository showSeatRepository) {
            this.bookingRepository = bookingRepository;
            this.bookingSeatRepository = bookingSeatRepository;
            this.seatRepository = seatRepository;
            this.showRepository = showRepository;
            this.showSeatRepository = showSeatRepository;
        }

        public Booking bookSeats(Long showId, List<Long> seatIds, User user) {

            Show show = showRepository.findById(showId) //ensures show exists
                    .orElseThrow(() -> new RuntimeException("Show not found"));

            List<ShowSeat> showSeats = showSeatRepository.findWithLock(showId, seatIds);// fetch seats with lock

            for (ShowSeat ss : showSeats) {
                if (ss.getSeatStatus() != SeatStatus.AVAILABLE) {
                    throw new RuntimeException("Seat not available");
                } // for my temporary use
                ss.setSeatStatus(SeatStatus.BOOKED);//marked immediately after booking
            }

            showSeatRepository.saveAll(showSeats);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setShow(show);
            booking.setBookedAt(LocalDateTime.now());

            booking = bookingRepository.save(booking);

            for (ShowSeat ss : showSeats) {
                BookingSeat bs = new BookingSeat();
                bs.setBooking(booking);
                bs.setSeat(ss.getSeat());
                bs.setShow(show);

                bookingSeatRepository.save(bs);
            }

            return booking;
        }

        public Optional<Booking> getBookingById(Long id) {
            return bookingRepository.findById(id);
        }

        public List<Booking> getAllBookings() {
            return bookingRepository.findAll();
        }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
        }
}
