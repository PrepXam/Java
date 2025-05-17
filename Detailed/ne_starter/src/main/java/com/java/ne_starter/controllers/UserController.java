package com.java.ne_starter.controllers;

import com.java.ne_starter.dtos.auth.LoginDto;
import com.java.ne_starter.dtos.auth.AuthResponse;
import com.java.ne_starter.dtos.auth.RegisterDto;
import com.java.ne_starter.dtos.email.ResendOtpDto;
import com.java.ne_starter.dtos.email.VerifyEmailDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.services.interfaces.AuthenticationService;
import com.java.ne_starter.services.interfaces.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterDto registerDto) {
        return authenticationService.register(registerDto);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @RequestBody VerifyEmailDto request) {
        String email = request.getEmail();
        String otp = request.getOtp();
        boolean isVerified = userService.verifyOtp(email, otp);
        if (isVerified) {
            return ApiResponse.success(
                    "Email successfully verified",
                    HttpStatus.OK,
                    "Verification successful"
            );
        } else {
            return ApiResponse.error(
                    "Email not verified",
                    HttpStatus.BAD_REQUEST,
                    "Invalid or expired OTP"
            );
        }
    }


    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody ResendOtpDto request) {
        boolean otpSent = userService.resendOtp(request.getEmail());

        if (otpSent) {
            return ApiResponse.success(
                    "New OTP sent successfully",
                    HttpStatus.OK,
                    "Check your email for the new OTP"
            );
        }
        return ApiResponse.error(
                "Error while sending OTP",
                HttpStatus.BAD_REQUEST,
                "Failed to resend OTP"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginDto loginDTO) {
        return authenticationService.login(loginDTO);
    }
}