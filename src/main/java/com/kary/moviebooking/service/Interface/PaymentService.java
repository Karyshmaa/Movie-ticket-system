package com.kary.moviebooking.service.Interface;

public interface PaymentService {
    String createRazorpayOrder(Long bookingId, Double amount);
}
