package com.java.ne_starter.controllers;

import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.vehicle.CreateVehicleDto;
import com.java.ne_starter.dtos.vehicle.VehicleDetailsDto;
import com.java.ne_starter.dtos.vehicle.VehicleResponseDto;
import com.java.ne_starter.services.interfaces.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<VehicleResponseDto>> registerVehicle(@Valid @RequestBody CreateVehicleDto dto) {
        return vehicleService.registerVehicle(dto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleDetailsDto>>> getAllVehiclesWithDetails() {
        return vehicleService.getAllVehiclesWithDetails();
    }


    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<VehicleResponseDto>>> getVehiclesByOwner(@PathVariable UUID ownerId) {
        return vehicleService.getVehiclesByOwner(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<VehicleDetailsDto>>> searchVehicles(
            @RequestParam String searchTerm) {
        return vehicleService.searchVehicles(searchTerm);
    }
}