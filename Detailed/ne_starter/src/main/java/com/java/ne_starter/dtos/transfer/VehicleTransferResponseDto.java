package com.java.ne_starter.dtos.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VehicleTransferResponseDto {
    private UUID id;
    private UUID vehicleId;
    private String previousOwnerName;
    private String newOwnerName;
    private String oldPlateNumber;
    private String newPlateNumber;
    private BigDecimal price;
    private LocalDateTime transferDate;
}
