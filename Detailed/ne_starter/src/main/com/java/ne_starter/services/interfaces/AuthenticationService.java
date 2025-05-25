package com.java.ne_starter.services.interfaces;

import com.java.ne_starter.dtos.auth.AuthResponse;
import com.java.ne_starter.dtos.auth.LoginDto;
import com.java.ne_starter.dtos.auth.RegisterDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<ApiResponse<AuthResponse>> login(LoginDto loginDTO);
    ResponseEntity<ApiResponse<AuthResponse>> register(RegisterDto registerUserDTO);
}
