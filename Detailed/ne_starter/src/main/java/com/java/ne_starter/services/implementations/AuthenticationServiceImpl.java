package com.java.ne_starter.services.implementations;

import com.java.ne_starter.dtos.auth.AuthResponse;
import com.java.ne_starter.dtos.auth.LoginDto;
import com.java.ne_starter.dtos.auth.RegisterDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.user.CreateUserDto;
import com.java.ne_starter.dtos.user.UserResponseDto;
import com.java.ne_starter.enumerations.user.EUserRole;
import com.java.ne_starter.models.User;
import com.java.ne_starter.repositories.UserRepository;
import com.java.ne_starter.security.JwtTokenProvider;
import com.java.ne_starter.security.UserPrincipal;
import com.java.ne_starter.services.interfaces.AuthenticationService;
import com.java.ne_starter.services.interfaces.UserService;
import com.java.ne_starter.utils.UserUtils;
import com.java.ne_starter.exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> register(RegisterDto registerDto) {
        try {
            // Create user DTO from register request
            CreateUserDto createUserDTO = CreateUserDto.fromRegisterDto(registerDto);

            // Call userService to create user
            ResponseEntity<?> responseEntity = userService.createUser(createUserDTO);

            // Extract userId from returned UserResponseDto
            UserResponseDto userResponse = (UserResponseDto) responseEntity.getBody();
            User user = userRepository.findById(userResponse.getId()).orElseThrow();

            // Authenticate the new user
            Authentication authentication = authenticateUser(
                    new LoginDto(createUserDTO.getEmail(), createUserDTO.getPassword())
            );

            // Generate JWT response
            AuthResponse response = generateJwtAuthenticationResponse(authentication);

            // Return wrapped API response
            return ApiResponse.success("Successfully registered user", HttpStatus.OK, response);

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    public ResponseEntity<ApiResponse<AuthResponse>> login(LoginDto loginDTO) {
        try {
            // First check if email is verified
            User user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            if (!user.isEmailVerified()) {
                boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getName() == EUserRole.ROLE_ADMIN);

                if (!isAdmin) {
                    throw new CustomException("Email not verified. Please verify your email first.");
                }
            }

            Authentication authentication = authenticateUser(loginDTO);
            AuthResponse response = generateJwtAuthenticationResponse(authentication);
            return ApiResponse.success("Successfully logged in", HttpStatus.OK, response);
        } catch (CustomException e) {
            throw e; // Re-throw CustomException as is
        } catch (Exception e) {
            throw new CustomException("Login failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Authentication authenticateUser(LoginDto loginDTO) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private AuthResponse generateJwtAuthenticationResponse(Authentication authentication) {
        String jwt = jwtTokenProvider.generateAccessToken(authentication);
        UserPrincipal userPrincipal = UserUtils.getLoggedInUser();
        User user = userService.findUserById(userPrincipal.getId());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        return new AuthResponse(jwt, user);
    }
}
