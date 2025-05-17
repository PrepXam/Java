package com.java.ne_starter.dtos.owner;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateOwnerDto {
    @NotBlank
    private String name;

    @NotBlank
    private String nationalId;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;
}
