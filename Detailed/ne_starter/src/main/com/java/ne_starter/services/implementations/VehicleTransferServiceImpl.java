package com.java.ne_starter.services.implementations;

import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.dtos.transfer.VehicleTransferDto;
import com.java.ne_starter.dtos.transfer.VehicleTransferResponseDto;
import com.java.ne_starter.enumerations.plate.PlateStatus;
import com.java.ne_starter.exceptions.NotFoundException;
import com.java.ne_starter.models.*;
import com.java.ne_starter.repositories.*;

import com.java.ne_starter.models.Owner;
import com.java.ne_starter.models.PlateNumber;
import com.java.ne_starter.models.Vehicle;
import com.java.ne_starter.models.VehicleTransfer;
import com.java.ne_starter.repositories.OwnerRepository;
import com.java.ne_starter.repositories.PlateNumberRepository;
import com.java.ne_starter.repositories.VehicleRepository;
import com.java.ne_starter.repositories.VehicleTransferRepository;
import com.java.ne_starter.services.interfaces.VehicleHistoryService;
import com.java.ne_starter.services.interfaces.VehicleTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleTransferServiceImpl implements VehicleTransferService {

    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final PlateNumberRepository plateNumberRepository;
    private final VehicleTransferRepository transferRepository;
    private final VehicleHistoryService historyService;


    @Override
    @Transactional
    public ResponseEntity<ApiResponse<VehicleTransferResponseDto>> transferVehicle(VehicleTransferDto dto) {
        // Validate and fetch entities
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        Owner newOwner = ownerRepository.findById(dto.getNewOwnerId())
                .orElseThrow(() -> new NotFoundException("New owner not found"));

        // Verify new plate belongs to new owner and is available
        PlateNumber newPlate = plateNumberRepository.findByIdAndOwnerId(dto.getNewPlateId(), newOwner.getId())
                .orElseThrow(() -> new NotFoundException("Plate number not found or doesn't belong to new owner"));

//        if (newPlate.getStatus() != PlateStatus.AVAILABLE) {
//            throw new IllegalStateException("Selected plate number is already in use");
//        }

        // Process the transfer
        processPlateNumberTransfer(vehicle, newPlate);
        Owner previousOwner = updateVehicleOwnership(vehicle, newOwner, newPlate);
        VehicleTransfer transfer = createTransferRecord(vehicle, previousOwner, newOwner, newPlate, dto.getPrice());

        historyService.recordOwnershipChange(
                vehicle.getId(),
                previousOwner.getId(),
                newOwner.getId(),
                dto.getPrice(),
                LocalDateTime.now()
        );

        // Prepare response
        VehicleTransferResponseDto response = buildTransferResponse(transfer, vehicle, previousOwner, newOwner);

        return ApiResponse.success("Vehicle transferred successfully", HttpStatus.OK, response);
    }

    private void processPlateNumberTransfer(Vehicle vehicle, PlateNumber newPlate) {
        // Release old plate
        PlateNumber oldPlate = vehicle.getVehiclePlateNumber();
        if (oldPlate != null) {
            oldPlate.setStatus(PlateStatus.AVAILABLE);
            oldPlate.setVehicle(null);
            plateNumberRepository.save(oldPlate);
        }

        // Assign new plate
        newPlate.setStatus(PlateStatus.ASSIGNED);
        newPlate.setVehicle(vehicle);
        plateNumberRepository.save(newPlate);
    }

    private Owner updateVehicleOwnership(Vehicle vehicle, Owner newOwner, PlateNumber newPlate) {
        Owner previousOwner = vehicle.getOwner();
        vehicle.setOwner(newOwner);
        vehicle.setVehiclePlateNumber(newPlate);
        vehicleRepository.save(vehicle);
        return previousOwner;
    }

    private VehicleTransfer createTransferRecord(Vehicle vehicle, Owner previousOwner, Owner newOwner,
                                                 PlateNumber newPlate, BigDecimal price) {
        VehicleTransfer transfer = new VehicleTransfer();
        transfer.setVehicle(vehicle);
        transfer.setPreviousOwner(previousOwner);
        transfer.setNewOwner(newOwner);
        transfer.setOldPlate(vehicle.getVehiclePlateNumber());
        transfer.setNewPlate(newPlate);
        transfer.setPrice(price);
        transfer.setTransferDate(LocalDateTime.now());
        return transferRepository.save(transfer);
    }

    private VehicleTransferResponseDto buildTransferResponse(VehicleTransfer transfer, Vehicle vehicle,
                                                             Owner previousOwner, Owner newOwner) {
        VehicleTransferResponseDto response = new VehicleTransferResponseDto();
        BeanUtils.copyProperties(transfer, response);
        response.setVehicleId(vehicle.getId());
        response.setPreviousOwnerName(previousOwner.getName());
        response.setNewOwnerName(newOwner.getName());
        response.setOldPlateNumber(transfer.getOldPlate() != null ? transfer.getOldPlate().getPlateNumber(): null);
        response.setNewPlateNumber(transfer.getNewPlate().getPlateNumber());
        return response;
    }
}