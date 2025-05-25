package com.java.ne_starter.dtos.owner;

import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class OwnerResponseDto {
    private UUID id;
    private String name;
    private String nationalId;
    private String email;
    private String phoneNumber;
    private String address;

    private List<String> plateNumbers;
}
