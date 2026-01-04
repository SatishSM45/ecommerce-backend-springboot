package org.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class UserResponse {
    private String mobileNo;

    private String name;

    private String email;

    private String gender;
    private LocalDate dateOfBirth;

    private boolean status ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
