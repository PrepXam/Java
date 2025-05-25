package com.java.ne_starter.services.interfaces;

import com.java.ne_starter.dtos.user.CreateUserDto;
import com.java.ne_starter.dtos.user.UserResponseDto;
import com.java.ne_starter.models.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface UserService {
    ResponseEntity<UserResponseDto> createUser(CreateUserDto dto);
    User findUserById(UUID id);
    boolean verifyOtp(String email, String otp);
    boolean resendOtp(String email);
}

