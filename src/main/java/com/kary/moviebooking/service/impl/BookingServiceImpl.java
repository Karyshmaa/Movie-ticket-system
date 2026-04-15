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
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingSeatRepository bookingSeatRepository,
                              ShowRepository showRepository,
                              ShowSeatRepository showSeatRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
    }


    public Booking createBooking(Long userId, Long showId) {

        // 1. Fetch locked seats for this user
        List<ShowSeat> seats = showSeatRepository.findLockedSeatsByUser(showId, userId);

        if (seats.isEmpty()) {
            throw new RuntimeException("No seats locked for this user");
        }

        // 2. Validate seats
        for (ShowSeat seat : seats) {

            if (seat.getSeatStatus() != SeatStatus.LOCKED) {
                throw new RuntimeException("Seat is not locked");
            }

            if (seat.getLockedAt() == null ||
                    seat.getLockedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {

                throw new RuntimeException("Seat lock expired");
            }
        }

        // 3. Get user & show
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // 4. Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookedAt(LocalDateTime.now());

        booking = bookingRepository.save(booking);

        // 5. Create BookingSeat + mark BOOKED
        for (ShowSeat seat : seats) {

            seat.setSeatStatus(SeatStatus.BOOKED);

            BookingSeat bs = new BookingSeat();
            bs.setBooking(booking);
            bs.setShowSeat(seat);   // ✅ IMPORTANT (new design)
            bs.setBookedAt(LocalDateTime.now());

            bookingSeatRepository.save(bs);
        }

        showSeatRepository.saveAll(seats);

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