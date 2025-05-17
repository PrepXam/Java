package com.java.ne_starter.repositories;

import com.java.ne_starter.models.VehicleTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleTransferRepository extends JpaRepository<VehicleTransfer, UUID> {
    List<VehicleTransfer> findByVehicleId(UUID vehicleId);
    List<VehicleTransfer> findByOldPlateIdOrNewPlateId(UUID oldPlateId, UUID newPlateId);

}