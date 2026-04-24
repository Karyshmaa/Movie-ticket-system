package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.service.Interface.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String createRazorpayOrder(Long bookingId, Double amount) {
        // Full Razorpay logic comes here — stub for now so red line goes away
        return "order_PLACEHOLDER_" + bookingId;
    }

}
