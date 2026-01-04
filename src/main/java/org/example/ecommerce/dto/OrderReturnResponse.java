package org.example.ecommerce.dto;

import lombok.Data;
import org.example.ecommerce.enums.OrderStatus;

@Data
public class OrderReturnResponse {
    private String OrderId;
    private String reason;
    private OrderStatus orderStatus;
}
