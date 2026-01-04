package org.example.ecommerce.dto;

import lombok.Data;
import org.example.ecommerce.enums.OrderStatus;
import org.example.ecommerce.enums.PaymentStatus;

@Data
public class OrderUpdateResponse {
    private String orderId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
}
