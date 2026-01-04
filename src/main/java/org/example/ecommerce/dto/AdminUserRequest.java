package org.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminUserRequest {
    private String mobileNo;
    private String name;
    private String gender;
    private String email;
    private String role;
    private String password;
    private LocalDate dateOfBirth;
}
