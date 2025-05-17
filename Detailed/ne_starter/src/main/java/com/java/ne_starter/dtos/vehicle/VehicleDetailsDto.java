package com.java.ne_starter.dtos.vehicle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class VehicleDetailsDto {

    private UUID id;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private String chassisNumber;
    private BigDecimal price;

    // Owner details
    private UUID ownerId;
    private String ownerName;
    private String ownerNationalId;
    private String ownerPhone;
    private String ownerAddress;

    // Plate number
    private UUID plateNumberId;
    private String plateNumber;
}
