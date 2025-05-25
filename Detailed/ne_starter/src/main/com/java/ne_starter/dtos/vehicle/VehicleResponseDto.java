package com.java.ne_starter.dtos.vehicle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class VehicleResponseDto {
    private UUID id;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private String chassisNumber;
    private UUID ownerId;
    private UUID plateNumberId;
    private BigDecimal price;
    private String ownerName;
}