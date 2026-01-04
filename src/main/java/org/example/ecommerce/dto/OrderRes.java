package org.example.ecommerce.dto;

import lombok.Data;
import org.example.ecommerce.enums.OrderStatus;
import org.example.ecommerce.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRes {
    private String orderId;
    private BigDecimal totalAmount;
    private String paymentMode;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
}
