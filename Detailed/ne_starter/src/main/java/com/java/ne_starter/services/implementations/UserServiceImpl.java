package com.java.ne_starter.services.implementations;

import com.java.ne_starter.dtos.user.CreateUserDto;
import com.java.ne_starter.dtos.user.UserResponseDto;
import com.java.ne_starter.enumerations.user.EUserRole;
import com.java.ne_starter.exceptions.ConflictException;
import com.java.ne_starter.models.Role;
import com.java.ne_starter.models.User;
import com.java.ne_starter.repositories.RoleRepository;
import com.java.ne_starter.repositories.UserRepository;
import com.java.ne_starter.services.interfaces.EmailService;
import com.java.ne_starter.services.interfaces.UserService;
import com.java.ne_starter.utils.HashUtil;
import com.java.ne_starter.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final int OTP_EXPIRATION_MINUTES = 15;
    private static final String OTP_VERIFICATION_FAILED = "Invalid or expired OTP";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Override
    @Transactional


    public ResponseEntity<UserResponseDto> createUser(CreateUserDto dto) {

        log.info("Attempting to create user with email: {}", dto.getEmail());


        // Check if email already exists BEFORE creating the user
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("User creation failed — email already exists: {}", dto.getEmail());
            throw new ConflictException("Email already exists");
        }

        // Create and populate the User entity
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setNationalId(dto.getNationalId());
        user.setPassword(HashUtil.hashPassword(dto.getPassword()));

        // Set role
        Role defaultRole = roleRepository.findByName(EUserRole.ROLE_STANDARD)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        String otp = OtpUtil.generateOtp();
        user.setOtp(otp);
        user.setEmailVerified(false);
        user.setOtpExpirationTime(LocalDateTime.now().plus(15, ChronoUnit.MINUTES));

        // Save the user
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), otp);

        log.info("User successfully created with email: {}", user.getEmail());

        // Prepare response
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setNationalId(user.getNationalId());
        response.setRoles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Check if already verified
        if (user.isEmailVerified()) {
            throw new ConflictException("Email already verified");
        }

        // Validate OTP
        if (user.getOtp() == null ||
                !user.getOtp().equals(otp) ||
                LocalDateTime.now().isAfter(user.getOtpExpirationTime())) {
            return false;
        }

        user.setEmailVerified(true);
        user.setOtp(null);
        user.setOtpExpirationTime(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            throw new ConflictException("Email already verified");
        }

        String newOtp = OtpUtil.generateOtp();
        user.setOtp(newOtp);
        user.setOtpExpirationTime(LocalDateTime.now().plus(OTP_EXPIRATION_MINUTES, ChronoUnit.MINUTES));
        userRepository.save(user);

        emailService.sendVerificationEmail(email, newOtp);
        return true;
    }


    @Override
    public User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }
}
