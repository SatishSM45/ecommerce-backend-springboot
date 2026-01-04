package org.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private Long addressId;
    private String paymentMode;

    private List<OrderItemRequest> items;
}
