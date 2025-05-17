package com.java.ne_starter.services.interfaces;

import com.java.ne_starter.dtos.plate.AssignPlateNumberDto;
import com.java.ne_starter.dtos.plate.CreateMultiplePlateNumbersDto;
import com.java.ne_starter.dtos.plate.PlateNumberResponseDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PlateNumberService {
    ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> createMultiplePlateNumbers(CreateMultiplePlateNumbersDto dto);
    ResponseEntity<ApiResponse<String>> assignPlateNumber(AssignPlateNumberDto dto);
    ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> getPlatesByOwner(UUID ownerId);
    ResponseEntity<ApiResponse<PlateNumberResponseDto>> getPlateByNumber(String plateNumber);
    ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> getAllPlateNumbers();
}