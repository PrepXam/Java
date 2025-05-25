package com.java.ne_starter.services.interfaces;

import com.java.ne_starter.dtos.history.OwnershipHistoryDto;
import com.java.ne_starter.models.VehicleOwnershipHistory;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface VehicleHistoryService {
    void recordOwnershipChange(UUID vehicleId, UUID previousOwnerId, UUID newOwnerId,
                               BigDecimal transferPrice, LocalDateTime transferDate);
    List<OwnershipHistoryDto> getVehicleHistory(String identifier);
    void saveOwnershipRecord(VehicleOwnershipHistory record);
}