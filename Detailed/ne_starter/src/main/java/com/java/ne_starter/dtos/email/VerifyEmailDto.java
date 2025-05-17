package com.java.ne_starter.dtos.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VerifyEmailDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String otp;
}
