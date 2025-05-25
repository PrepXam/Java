package com.java.ne_starter.services.implementations;

import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.vehicle.CreateVehicleDto;
import com.java.ne_starter.dtos.vehicle.VehicleDetailsDto;
import com.java.ne_starter.dtos.vehicle.VehicleResponseDto;
import com.java.ne_starter.exceptions.NotFoundException;
import com.java.ne_starter.models.Owner;
import com.java.ne_starter.models.PlateNumber;
import com.java.ne_starter.models.Vehicle;
import com.java.ne_starter.repositories.OwnerRepository;
import com.java.ne_starter.repositories.VehicleRepository;
import com.java.ne_starter.services.interfaces.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final OwnerRepository ownerRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public ResponseEntity<ApiResponse<VehicleResponseDto>> registerVehicle(CreateVehicleDto dto) {
        Owner owner = ownerRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setColor(dto.getColor());
        vehicle.setChassisNumber(dto.getChassisNumber());
        vehicle.setOwner(owner);
        vehicleRepository.save(vehicle);

        VehicleResponseDto response = new VehicleResponseDto();
        BeanUtils.copyProperties(vehicle, response);
        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getName());

        return ApiResponse.success("Vehicle registered successfully", HttpStatus.CREATED, response);
    }

    @Override
    public ResponseEntity<ApiResponse<List<VehicleResponseDto>>> getVehiclesByOwner(UUID ownerId) {
        List<Vehicle> vehicles = vehicleRepository.findByOwnerId(ownerId);

        List<VehicleResponseDto> responseList = vehicles.stream().map(vehicle -> {
            VehicleResponseDto dto = new VehicleResponseDto();
            BeanUtils.copyProperties(vehicle, dto);
            dto.setOwnerId(vehicle.getOwner().getId());
            dto.setOwnerName(vehicle.getOwner().getName());
            return dto;
        }).collect(Collectors.toList());

        return ApiResponse.success("Vehicles fetched successfully", HttpStatus.OK, responseList);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<VehicleDetailsDto>>> getAllVehiclesWithDetails() {
        // Fetch all vehicles with their owner and plate number relationships loaded
        List<Vehicle> vehicles = vehicleRepository.findAll();

        // Map to DTOs
        List<VehicleDetailsDto> response = vehicles.stream()
                .map(vehicle -> {
                    VehicleDetailsDto dto = new VehicleDetailsDto();

                    // Vehicle details
                    dto.setId(vehicle.getId());
                    dto.setMake(vehicle.getMake());
                    dto.setModel(vehicle.getModel());
                    dto.setYear(vehicle.getYear());
                    dto.setColor(vehicle.getColor());
                    dto.setChassisNumber(vehicle.getChassisNumber());
                    dto.setPrice(vehicle.getPrice());

                    // Owner details
                    Owner owner = vehicle.getOwner();
                    if (owner != null) {
                        dto.setOwnerId(owner.getId());
                        dto.setOwnerName(owner.getName());
                        dto.setOwnerNationalId(owner.getNationalId());
                        dto.setOwnerPhone(owner.getPhoneNumber());
                        dto.setOwnerAddress(owner.getAddress());
                    }

                    // Plate number details
                    PlateNumber plateNumber = vehicle.getVehiclePlateNumber();
                    if (plateNumber != null) {
                        dto.setPlateNumberId(plateNumber.getId());
                        dto.setPlateNumber(plateNumber.getPlateNumber()); // Assuming getNumber() exists
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return ApiResponse.success("All vehicles with details fetched successfully",
                HttpStatus.OK,
                response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<VehicleDetailsDto>>> searchVehicles(String searchTerm) {
        List<Vehicle> vehicles;

        // Determine search type based on input pattern
        if (searchTerm.matches("\\d+")) { // If it's all digits, search by national ID
            vehicles = vehicleRepository.findByOwner_NationalId(searchTerm);
        } else if (searchTerm.matches("[A-Za-z0-9]+")) { // Alphanumeric - could be plate number or chassis
            // Try plate number first
            vehicles = vehicleRepository.findByVehiclePlateNumber_PlateNumber(searchTerm);

            // If no results, try chassis number
            if (vehicles.isEmpty()) {
                vehicleRepository.findByChassisNumber(searchTerm)
                        .ifPresent(vehicle -> vehicles.add(vehicle));
            }
        } else {
            // If it doesn't match any pattern, return empty list
            vehicles = List.of();
        }

        // Map to DTOs
        List<VehicleDetailsDto> response = vehicles.stream()
                .map(vehicle -> {
                    VehicleDetailsDto dto = new VehicleDetailsDto();

                    // Vehicle details
                    dto.setId(vehicle.getId());
                    dto.setMake(vehicle.getMake());
                    dto.setModel(vehicle.getModel());
                    dto.setYear(vehicle.getYear());
                    dto.setColor(vehicle.getColor());
                    dto.setChassisNumber(vehicle.getChassisNumber());
                    dto.setPrice(vehicle.getPrice());

                    // Owner details
                    Owner owner = vehicle.getOwner();
                    if (owner != null) {
                        dto.setOwnerId(owner.getId());
                        dto.setOwnerName(owner.getName());
                        dto.setOwnerNationalId(owner.getNationalId());
                        dto.setOwnerPhone(owner.getPhoneNumber());
                        dto.setOwnerAddress(owner.getAddress());
                    }

                    // Plate number details
                    PlateNumber plateNumber = vehicle.getVehiclePlateNumber();
                    if (plateNumber != null) {
                        dto.setPlateNumberId(plateNumber.getId());
                        dto.setPlateNumber(plateNumber.getPlateNumber());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return ApiResponse.success("Vehicles fetched successfully", HttpStatus.OK, response);
    }
}