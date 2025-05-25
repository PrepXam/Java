package com.java.ne_starter.services.implementations;

import com.java.ne_starter.dtos.plate.AssignPlateNumberDto;
import com.java.ne_starter.dtos.plate.CreateMultiplePlateNumbersDto;
import com.java.ne_starter.dtos.plate.PlateNumberResponseDto;
import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.exceptions.BadRequestException;
import com.java.ne_starter.exceptions.ConflictException;
import com.java.ne_starter.exceptions.NotFoundException;
import com.java.ne_starter.models.Owner;
import com.java.ne_starter.models.PlateNumber;
import com.java.ne_starter.repositories.OwnerRepository;
import com.java.ne_starter.repositories.PlateNumberRepository;
import com.java.ne_starter.services.interfaces.PlateNumberService;
import com.java.ne_starter.enumerations.plate.PlateStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlateNumberServiceImpl implements PlateNumberService {

    private final PlateNumberRepository plateNumberRepository;
    private final OwnerRepository ownerRepository;


    @Override
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> createMultiplePlateNumbers(CreateMultiplePlateNumbersDto dto) {
        Set<String> existingPlates = plateNumberRepository.findAll()
                .stream()
                .map(PlateNumber::getPlateNumber)
                .collect(Collectors.toSet());

        List<PlateNumber> plateNumbers = new ArrayList<>();
        int baseIndex = existingPlates.size(); // Start after existing plates

        for (int i = 0; i < dto.getQuantity(); i++) {
            String plateNumber;
            int attempts = 0;

            do {
                plateNumber = generatePlateNumber(baseIndex + i + attempts);
                attempts++;
                if (attempts > 100) throw new RuntimeException("Generation failed");
            } while (existingPlates.contains(plateNumber));

            existingPlates.add(plateNumber);
            PlateNumber plate = new PlateNumber();
            plate.setPlateNumber(plateNumber);
            plate.setIssuedDate(LocalDate.now());
            plate.setStatus(PlateStatus.AVAILABLE);
            plateNumbers.add(plate);
        }


        plateNumberRepository.saveAll(plateNumbers);

        // Save all plate numbers to the database
        List<PlateNumberResponseDto> responseDtos = plateNumbers.stream()
                .map(plate -> {
                    PlateNumberResponseDto responseDto = new PlateNumberResponseDto();
                    responseDto.setId(plate.getId());
                    responseDto.setPlateNumber(plate.getPlateNumber());
                    responseDto.setStatus(plate.getStatus().name());
                    responseDto.setIssuedDate(plate.getIssuedDate());
                    return responseDto;
                })
                .collect(Collectors.toList());
        return ApiResponse.success("Plate numbers created successfully", HttpStatus.CREATED, responseDtos);
    }



    private String generatePlateNumber(int index) {
        // Generate plateNumber in the format "RAF123A"
        return String.format("RAF%03d%c",
                index + 1,
                'A' + (index % 26));
    }


    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> assignPlateNumber(AssignPlateNumberDto dto) {
        PlateNumber plate = plateNumberRepository.findById(dto.getPlateId())
                .orElseThrow(() -> new NotFoundException("Plate number not found"));


        if (plate.getStatus() != PlateStatus.AVAILABLE) {
            throw new ConflictException("Plate number is already assigned to an owner");
        }
        Owner owner = ownerRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        LocalDate today = LocalDate.now();
        if (!dto.getIssuedDate().isEqual(today)) {
            throw new BadRequestException("Issued date must be today's date");
        }

        // Assign the plate number to the owner
        plate.setOwner(owner);
        plate.setStatus(PlateStatus.ASSIGNED);
        plate.setIssuedDate(dto.getIssuedDate());

        // Update the status to assigned
        plateNumberRepository.save(plate);

        PlateNumberResponseDto response = new PlateNumberResponseDto();
        BeanUtils.copyProperties(plate, response);
        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getName());
        response.setStatus(plate.getStatus().name());
        return ApiResponse.success("Plate number assigned successfully", HttpStatus.OK, "Plate assigned");



    }





    public ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> getPlatesByOwner(UUID ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        List<PlateNumber> plates = plateNumberRepository.findByOwnerId(ownerId);

        List<PlateNumberResponseDto> responses = plates.stream().map(plate -> {
            PlateNumberResponseDto dto = new PlateNumberResponseDto();
            dto.setId(plate.getId());
            dto.setPlateNumber(plate.getPlateNumber());
            dto.setStatus(plate.getStatus().name());
            dto.setOwnerId(owner.getId());
            dto.setOwnerName(owner.getName());
            dto.setIssuedDate(plate.getIssuedDate());
            return dto;
        }).collect(Collectors.toList());

        return ApiResponse.success("Plate numbers for owner fetched", HttpStatus.OK, responses);
    }

    @Override
    public ResponseEntity<ApiResponse<PlateNumberResponseDto>> getPlateByNumber(String plateNumber) {
        PlateNumber plate = plateNumberRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new NotFoundException("Plate number not found"));

        PlateNumberResponseDto dto = new PlateNumberResponseDto();
        dto.setId(plate.getId());
        dto.setPlateNumber(plate.getPlateNumber());
        dto.setStatus(plate.getStatus().name());
        dto.setOwnerId(plate.getOwner().getId());
        dto.setOwnerName(plate.getOwner().getName());
        dto.setIssuedDate(plate.getIssuedDate());

        return ApiResponse.success("Plate number found", HttpStatus.OK, dto);
    }
    @Override
    public ResponseEntity<ApiResponse<List<PlateNumberResponseDto>>> getAllPlateNumbers() {
        List<PlateNumber> plateNumbers = plateNumberRepository.findAll();



        List<PlateNumberResponseDto> responseList = plateNumbers.stream().map(plate -> {
            PlateNumberResponseDto dto = new PlateNumberResponseDto();
            BeanUtils.copyProperties(plate, dto);
            dto.setStatus(plate.getStatus().name());

            return dto;
        }).collect(Collectors.toList());
        return ApiResponse.success("List of all plate numbers", HttpStatus.OK, responseList);


    }

}
