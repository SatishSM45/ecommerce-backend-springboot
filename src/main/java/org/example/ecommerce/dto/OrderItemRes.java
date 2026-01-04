package org.example.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRes {
    private String productName;
    private Integer quantity;
    private BigDecimal price;   // snapshot price
    private BigDecimal totalPrice;
}
