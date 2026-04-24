package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.BookingRequestDTO;
import com.kary.moviebooking.dto.BookingResponseDTO;
import com.kary.moviebooking.entity.*;
import com.kary.moviebooking.enums.BookingStatus;
import com.kary.moviebooking.enums.SeatStatus;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.exception.SeatNotAvailableException;
import com.kary.moviebooking.repository.*;
import com.kary.moviebooking.service.Interface.BookingService;
import com.kary.moviebooking.service.Interface.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;


    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));

        return BookingResponseDTO.builder()
                .bookingId(booking.getId())
                .showId(booking.getShow().getId())
                .userId(booking.getUser().getId())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .bookedAt(booking.getBookedAt())
                .build();
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(booking -> BookingResponseDTO.builder()
                        .bookingId(booking.getId())
                        .showId(booking.getShow().getId())
                        .userId(booking.getUser().getId())
                        .totalAmount(booking.getTotalAmount())
                        .status(booking.getStatus().name())
                        .bookedAt(booking.getBookedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookingResponseDTO initiateBooking(BookingRequestDTO request, String username) {

        // 1. Fetch user from JWT username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Fetch show
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        // 3. Fetch and validate requested seats
        List<ShowSeat> seats = showSeatRepository.findAllById(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more seats not found");
        }

        // 4. Check all seats are AVAILABLE — throws if any is taken
        seats.forEach(seat -> {
            if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new SeatNotAvailableException(
                        "Seat " + seat.getId() + " is not available"
                );
            }
        });

        // 5. Lock all seats as TEMP_LOCKED
        seats.forEach(seat -> {
            seat.setSeatStatus(SeatStatus.TEMP_LOCKED);
            seat.setLockedAt(LocalDateTime.now());
            seat.setLockedByUserId(user.getId());
        });
        showSeatRepository.saveAll(seats);

        // 6. Calculate total amount
        double total = seats.stream()
                .mapToDouble(s -> s.getPrice())
                .sum();

        // 7. Create PENDING booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setStatus(BookingStatus.PENDING);          // ✅ PENDING, not confirmed yet
        booking.setTotalAmount(total);
        booking.setBookedAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // 8. Save BookingSeats join records
        Booking finalBooking = booking;
        List<BookingSeat> bookingSeats = seats.stream().map(seat -> {
            BookingSeat bs = new BookingSeat();
            bs.setBooking(finalBooking);
            bs.setShowSeat(seat);
            bs.setBookedAt(LocalDateTime.now());
            return bs;
        }).collect(Collectors.toList());
        bookingSeatRepository.saveAll(bookingSeats);

        // 9. Create Razorpay order
        String razorpayOrderId = paymentService.createRazorpayOrder(
                booking.getId(), total
        );

        // 10. Build and return response
        return BookingResponseDTO.builder()
                .bookingId(booking.getId())
                .showId(show.getId())
                .userId(user.getId())
                .totalAmount(total)
                .status(BookingStatus.PENDING.name())
                .razorpayOrderId(razorpayOrderId)          // ✅ frontend uses this
                .lockedSeatIds(request.getSeatIds())
                .bookedAt(booking.getBookedAt())
                .build();
    }

    // ... getBookingById, getAllBookings, deleteBooking map to DTOs too
}

