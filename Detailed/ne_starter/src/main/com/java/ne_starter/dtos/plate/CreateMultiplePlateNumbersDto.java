package com.java.ne_starter.dtos.plate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateMultiplePlateNumbersDto {
    @NotNull
    private int quantity; // Number of plate numbers to create
}
