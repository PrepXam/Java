package com.java.ne_starter.services.interfaces;

import com.java.ne_starter.dtos.owner.CreateOwnerDto;
import com.java.ne_starter.dtos.owner.OwnerResponseDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface OwnerService {
    ResponseEntity<ApiResponse<OwnerResponseDto>> createOwner(CreateOwnerDto dto);

    ResponseEntity<ApiResponse<OwnerResponseDto>> getOwnerByNationalId(String nationalId);

    ResponseEntity<ApiResponse<OwnerResponseDto>> getOwnerByEmail(String email);

    ResponseEntity<ApiResponse<OwnerResponseDto>> getOwnerByPhone(String phone);

    ResponseEntity<ApiResponse<List<OwnerResponseDto>>> getAllOwners(int page, int size);

    ResponseEntity<ApiResponse<List<String>>> getPlateNumbersByOwner(UUID ownerId);

}