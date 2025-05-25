package com.java.ne_starter.services.implementations;


import com.java.ne_starter.dtos.history.OwnershipHistoryDto;
import com.java.ne_starter.models.PlateNumber;
import com.java.ne_starter.models.Vehicle;
import com.java.ne_starter.models.VehicleOwnershipHistory;
import com.java.ne_starter.repositories.OwnerRepository;
import com.java.ne_starter.repositories.PlateNumberRepository;
import com.java.ne_starter.repositories.VehicleOwnershipHistoryRepository;
import com.java.ne_starter.repositories.VehicleRepository;
import com.java.ne_starter.services.interfaces.VehicleHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleHistoryServiceImpl implements VehicleHistoryService {
    private final VehicleOwnershipHistoryRepository historyRepository;
    private final OwnerRepository ownerRepository;
    private final VehicleRepository vehicleRepository;
    private final PlateNumberRepository plateNumberRepository;

    @Override
    @Transactional
    public void recordOwnershipChange(UUID vehicleId, UUID previousOwnerId, UUID newOwnerId,
                                      BigDecimal transferPrice, LocalDateTime transferDate) {
        log.info("Recording ownership change for vehicle: {}, previous owner: {}, new owner: {}",
                vehicleId, previousOwnerId, newOwnerId);

        // Close current ownership period
        historyRepository.findFirstByVehicleIdAndEndDateIsNull(vehicleId)
                .ifPresent(current -> {
                    log.info("Closing previous ownership record: {}", current.getId());
                    current.setEndDate(transferDate);
                    historyRepository.save(current);
                });

        // Create new ownership record
        VehicleOwnershipHistory newHistory = new VehicleOwnershipHistory();
        newHistory.setVehicle(vehicleRepository.getReferenceById(vehicleId));
        newHistory.setOwner(ownerRepository.getReferenceById(newOwnerId));
        newHistory.setPrevOwner(ownerRepository.getReferenceById(previousOwnerId));
        newHistory.setStartDate(LocalDateTime.now());
        newHistory.setPurchasePrice(transferPrice);
        newHistory.setEndDate(transferDate);

        historyRepository.save(newHistory);
        log.info("Created new ownership record: {}", newHistory.getId());
    }

    @Override
    @Transactional
    public void saveOwnershipRecord(VehicleOwnershipHistory record) {
        try {
            historyRepository.save(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnershipHistoryDto> getVehicleHistory(String identifier) {
        try {
            UUID vehicleId = UUID.fromString(identifier);
            return getHistoryByVehicleId(vehicleId);
        } catch (IllegalArgumentException e) {
            // Not a UUID, try other identifiers
        }

        // Try by plate number
        Optional<PlateNumber> plateOpt = plateNumberRepository.findByPlateNumber(identifier);
        if (plateOpt.isPresent()) {
            Optional<Vehicle> vehicleOpt = vehicleRepository.findByVehiclePlateNumber(plateOpt.get());
            if (vehicleOpt.isPresent()) {
                return getHistoryByVehicleId(vehicleOpt.get().getId());
            }
            return Collections.emptyList();
        }

        // Try by chassis number
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByChassisNumber(identifier);
        if (vehicleOpt.isPresent()) {
            return getHistoryByVehicleId(vehicleOpt.get().getId());
        }

        return Collections.emptyList();
    }

    private List<OwnershipHistoryDto> getHistoryByVehicleId(UUID vehicleId) {
        List<VehicleOwnershipHistory> history = historyRepository.findByVehicleIdOrderByStartDateDesc(vehicleId);
        return history.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OwnershipHistoryDto convertToDto(VehicleOwnershipHistory history) {
        OwnershipHistoryDto dto = new OwnershipHistoryDto();
        dto.setOwnerName(history.getOwner().getName());
        dto.setOwnerNationalId(history.getOwner().getNationalId());
        dto.setStartDate(history.getStartDate());
        dto.setEndDate(history.getEndDate());
        dto.setPurchasePrice(history.getPurchasePrice());
        return dto;
    }
}