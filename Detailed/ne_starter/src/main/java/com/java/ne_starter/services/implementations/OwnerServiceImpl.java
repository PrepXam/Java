package com.java.ne_starter.services.implementations;

import com.java.ne_starter.dtos.owner.CreateOwnerDto;
import com.java.ne_starter.dtos.owner.OwnerResponseDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.exceptions.ConflictException;
import com.java.ne_starter.exceptions.NotFoundException;
import com.java.ne_starter.models.Owner;
import com.java.ne_starter.models.PlateNumber;
import com.java.ne_starter.repositories.OwnerRepository;
import com.java.ne_starter.services.interfaces.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    @Override
    public ResponseEntity<ApiResponse<OwnerResponseDto>> createOwner(CreateOwnerDto dto) {
        if (ownerRepository.findByNationalId(dto.getNationalId()).isPresent()) {
            throw new ConflictException("Owner with this national ID already exists");
        }

        Owner owner = new Owner();
        owner.setName(dto.getName());
        owner.setNationalId(dto.getNationalId());
        owner.setEmail(dto.getEmail());
        owner.setPhoneNumber(dto.getPhoneNumber());
        owner.setAddress(dto.getAddress());

        ownerRepository.save(owner);

        OwnerResponseDto response = new OwnerResponseDto();
        BeanUtils.copyProperties(owner, response);


        return ApiResponse.success("Owner registered successfully", HttpStatus.CREATED, response);

    }

    @Override
    public ResponseEntity<ApiResponse<OwnerResponseDto>> getOwnerByNationalId(String nationalId) {
        Owner owner = ownerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new NotFoundException("Owner not found"));
        OwnerResponseDto response = new OwnerResponseDto();
        BeanUtils.copyProperties(owner, response);
        return ApiResponse.success("Owner found", HttpStatus.OK, response);
    }

    @Override
    public ResponseEntity<ApiResponse<OwnerResponseDto>> getOwnerByEmail(String email) {
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Owner not found"));
        OwnerResponseDto response = new OwnerResponseDto();
        BeanUtils.copyProperties(owner, response);
        return ApiResponse.success("Owner found", HttpStatus.OK, response);
    }

    @Override
    public ResponseEntity<ApiResponse<OwnerResponseDto>> getOwnerByPhone(String phone) {
        Owner owner = ownerRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new NotFoundException("Owner not found"));
        OwnerResponseDto response = new OwnerResponseDto();
        BeanUtils.copyProperties(owner, response);
        return ApiResponse.success("Owner found", HttpStatus.OK, response);
    }

    @Override
    public ResponseEntity<ApiResponse<List<OwnerResponseDto>>> getAllOwners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<OwnerResponseDto> owners = ownerRepository.findAll(pageable)
                .map(owner -> {
                    OwnerResponseDto dto = new OwnerResponseDto();
                    BeanUtils.copyProperties(owner, dto);
                    return dto;
                }).getContent();

        return ApiResponse.success("List of owners", HttpStatus.OK, owners);
    }

    @Override
    public ResponseEntity<ApiResponse<List<String>>> getPlateNumbersByOwner(UUID ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        List<String> plateNumbers = owner.getPlateNumbers().stream()
                .map(PlateNumber::getPlateNumber)
                .collect(Collectors.toList());

        return ApiResponse.success("Plate numbers fetched", HttpStatus.OK ,plateNumbers);
    }

}
