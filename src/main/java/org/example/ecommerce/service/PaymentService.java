package org.example.ecommerce.service;

import org.example.ecommerce.dto.PaymentRequest;
import org.example.ecommerce.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse doPayment(PaymentRequest request);
}
