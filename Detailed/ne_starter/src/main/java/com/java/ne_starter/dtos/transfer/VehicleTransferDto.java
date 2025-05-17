package com.java.ne_starter.dtos.transfer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class VehicleTransferDto {
    @NotNull
    private UUID vehicleId;

    @NotNull
    private UUID newOwnerId;

    @NotNull
    private UUID newPlateId;

    @NotNull
    private BigDecimal price;
}