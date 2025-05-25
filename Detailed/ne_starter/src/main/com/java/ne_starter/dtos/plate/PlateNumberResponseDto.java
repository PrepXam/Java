package com.java.ne_starter.dtos.plate;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PlateNumberResponseDto {
    private UUID id;
    private String plateNumber;
    private String status;
    private LocalDate issuedDate;

    private UUID ownerId;
    private String ownerName;
}
