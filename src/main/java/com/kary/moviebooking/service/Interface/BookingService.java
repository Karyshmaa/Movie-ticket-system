package com.kary.moviebooking.service.Interface;

import com.kary.moviebooking.entity.Booking;
import com.kary.moviebooking.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking createBooking(Long userId, Long showId);

    Optional<Booking> getBookingById(Long id);

    List<Booking> getAllBookings();

    void deleteBooking(Long id);
}
