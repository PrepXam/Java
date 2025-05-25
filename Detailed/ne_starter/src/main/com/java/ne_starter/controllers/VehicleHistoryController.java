package com.java.ne_starter.controllers;

import com.java.ne_starter.dtos.history.OwnershipHistoryDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.services.interfaces.VehicleHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle-history")
@RequiredArgsConstructor
public class VehicleHistoryController {
    private final VehicleHistoryService historyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OwnershipHistoryDto>>> getVehicleHistory(
            @RequestParam String identifier) {
        List<OwnershipHistoryDto> history = historyService.getVehicleHistory(identifier);

        System.out.println(history.size() + " history items retrieved");

        return ApiResponse.success(
                "Ownership history retrieved successfully",
                HttpStatus.OK,
                history
        );
    }
}
