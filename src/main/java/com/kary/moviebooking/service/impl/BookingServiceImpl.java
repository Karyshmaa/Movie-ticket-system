package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.entity.*;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.repository.*;
import com.kary.moviebooking.service.Interface.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingSeatRepository bookingSeatRepository,
                              ShowRepository showRepository,
                              ShowSeatRepository showSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
    }

    @Override
    public Booking bookSeats(Long showId, List<Long> seatIds, User user) {

        // 1. Get show
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // 2. Lock seats
        List<ShowSeat> showSeats = showSeatRepository.findWithLock(showId, seatIds);

        // 3. Check + mark booked
        for (ShowSeat ss : showSeats) {
            if (ss.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat not available");
            }
            ss.setSeatStatus(SeatStatus.BOOKED);
        }

        showSeatRepository.saveAll(showSeats);

        // 4. Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookedAt(LocalDateTime.now());

        booking = bookingRepository.save(booking);

        // 5. Map seats
        for (ShowSeat ss : showSeats) {
            BookingSeat bs = new BookingSeat();
            bs.setBooking(booking);
            bs.setSeat(ss.getSeat());
            bs.setShow(show);

            bookingSeatRepository.save(bs);
        }

        return booking;
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}