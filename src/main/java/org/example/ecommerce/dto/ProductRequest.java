package org.example.ecommerce.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Double price;
    private Integer stock;
    private Long category; //optional
    private Boolean status;
}
