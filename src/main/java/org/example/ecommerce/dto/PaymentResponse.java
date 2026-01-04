package org.example.ecommerce.dto;

import lombok.Data;
import org.example.ecommerce.enums.PaymentStatus;

@Data
public class PaymentResponse {

    private String orderId;
    private String transactionId;
    private PaymentStatus status;
    private String message;

    // getters & setters
}

