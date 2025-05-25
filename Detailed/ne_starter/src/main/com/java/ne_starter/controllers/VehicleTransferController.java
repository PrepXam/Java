package com.java.ne_starter.controllers;

import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.transfer.VehicleTransferDto;
import com.java.ne_starter.dtos.transfer.VehicleTransferResponseDto;
import com.java.ne_starter.services.interfaces.VehicleTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class VehicleTransferController {

    private final VehicleTransferService vehicleTransferService;

    @PostMapping("/vehicle")
    public ResponseEntity<ApiResponse<VehicleTransferResponseDto>> transferVehicle(@Valid @RequestBody VehicleTransferDto dto) {
        return vehicleTransferService.transferVehicle(dto);
    }


}