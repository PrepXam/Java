package com.java.ne_starter.services.interfaces;

import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.vehicle.CreateVehicleDto;
import com.java.ne_starter.dtos.vehicle.VehicleDetailsDto;
import com.java.ne_starter.dtos.vehicle.VehicleResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface VehicleService {
    ResponseEntity<ApiResponse<VehicleResponseDto>> registerVehicle(CreateVehicleDto dto);
    ResponseEntity<ApiResponse<List<VehicleResponseDto>>> getVehiclesByOwner(UUID ownerId);
    ResponseEntity<ApiResponse<List<VehicleDetailsDto>>> getAllVehiclesWithDetails();
    ResponseEntity<ApiResponse<List<VehicleDetailsDto>>> searchVehicles(String searchTerm);
}