package org.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
    private String mobileNo;

    private String name;
    @NotBlank
    private String email; //mainIdentity
    private String password;
    private String gender;
    private LocalDate dateOfBirth;
}
