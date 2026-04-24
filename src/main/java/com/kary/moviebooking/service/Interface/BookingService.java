package com.kary.moviebooking.service.Interface;

import com.kary.moviebooking.dto.BookingRequestDTO;
import com.kary.moviebooking.dto.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO initiateBooking(BookingRequestDTO request, String username);

    BookingResponseDTO getBookingById(Long id);

    List<BookingResponseDTO> getAllBookings();

    void deleteBooking(Long id);
}
