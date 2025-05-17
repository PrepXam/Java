package com.java.ne_starter.controllers;


import com.java.ne_starter.dtos.plate.AssignPlateNumberDto;
import com.java.ne_starter.dtos.plate.CreateMultiplePlateNumbersDto;
import com.java.ne_starter.dtos.plate.PlateNumberResponseDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.services.interfaces.PlateNumberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plates")
@RequiredArgsConstructor
public class PlateNumberController {

    private final PlateNumberService plateNumberService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> createMultiplePlateNumbers(@Valid @RequestBody CreateMultiplePlateNumbersDto dto) {
        return plateNumberService.createMultiplePlateNumbers(dto);
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<String>> assignPlateNumber(@Valid @RequestBody AssignPlateNumberDto dto) {
        return plateNumberService.assignPlateNumber(dto);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> getByOwner(@PathVariable UUID ownerId) {
        return plateNumberService.getPlatesByOwner(ownerId);
    }

    @GetMapping("/search/{plateNumber}")
    public ResponseEntity<ApiResponse<PlateNumberResponseDto>> getByPlate(@PathVariable String plateNumber) {
        return plateNumberService.getPlateByNumber(plateNumber);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> getAllPlateNumbers() {
        return plateNumberService.getAllPlateNumbers();
    }

}
