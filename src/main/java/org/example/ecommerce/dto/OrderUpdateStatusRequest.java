package org.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderUpdateStatusRequest {
    @NotBlank(message = "Not blank the status")
    private String status;
}
