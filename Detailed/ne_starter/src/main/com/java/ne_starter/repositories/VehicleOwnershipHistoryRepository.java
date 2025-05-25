package com.java.ne_starter.repositories;

import com.java.ne_starter.models.VehicleOwnershipHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleOwnershipHistoryRepository extends JpaRepository<VehicleOwnershipHistory, UUID> {
    Optional<VehicleOwnershipHistory> findFirstByVehicleIdAndEndDateIsNull(UUID vehicleId);
    @Query("SELECT voh FROM VehicleOwnershipHistory voh " +
            "JOIN voh.vehicle v " +
            "WHERE v.chassisNumber = :chassisNumber " +
            "ORDER BY voh.startDate DESC")
    List<VehicleOwnershipHistory> findOwnershipHistoryByChassisNumberOrderByStartDateDesc(@Param("chassisNumber") String chassisNumber);
    @Query("SELECT h FROM VehicleOwnershipHistory h WHERE h.vehicle.vehiclePlateNumber.plateNumber = :plateNumber ORDER BY h.startDate DESC")
    List<VehicleOwnershipHistory> findByVehicleVehiclePlateNumberPlateNumberOrderByStartDateDesc(@Param("plateNumber") String plateNumber);
    List<VehicleOwnershipHistory> findByVehicleIdOrderByStartDateDesc(UUID vehicleId);
}
