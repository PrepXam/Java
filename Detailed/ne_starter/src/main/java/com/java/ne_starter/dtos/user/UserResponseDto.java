package com.java.ne_starter.dtos.user;


import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String nationalId;
    private String phoneNumber;
    private Set<String> roles;
}
