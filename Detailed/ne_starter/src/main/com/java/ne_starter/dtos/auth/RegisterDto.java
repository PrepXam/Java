package com.java.ne_starter.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    private String phoneNumber;
    private String nationalId;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
