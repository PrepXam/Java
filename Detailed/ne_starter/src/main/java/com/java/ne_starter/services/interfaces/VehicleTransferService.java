package com.java.ne_starter.services.interfaces;


import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.transfer.VehicleTransferDto;
import com.java.ne_starter.dtos.transfer.VehicleTransferResponseDto;
import org.springframework.http.ResponseEntity;

public interface VehicleTransferService {
    ResponseEntity<ApiResponse<VehicleTransferResponseDto>> transferVehicle(VehicleTransferDto dto);
}