package org.example.ecommerce.dto;


import lombok.*;
import org.example.ecommerce.enums.OrderStatus;
import org.example.ecommerce.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OrderResponse {

    private String orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDate;
    private String paymentMode;
    private List<OrderItemResponse> items;
}
