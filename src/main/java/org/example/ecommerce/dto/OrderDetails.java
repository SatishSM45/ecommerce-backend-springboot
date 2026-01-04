package org.example.ecommerce.dto;

import lombok.Data;

@Data
public class OrderDetails {
    private Long productId;
    private Double totalAmount;
    private Integer quantity;
}
