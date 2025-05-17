package com.java.ne_starter.repositories;

import com.java.ne_starter.models.PlateNumber;
import com.java.ne_starter.models.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    List<Vehicle> findByOwnerId(UUID ownerId);
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    @EntityGraph(attributePaths = {"owner", "vehiclePlateNumber"})
    @Override
    List<Vehicle> findAll();

    @EntityGraph(attributePaths = {"owner", "vehiclePlateNumber"})
    List<Vehicle> findByOwner_NationalId(String nationalId);

    @EntityGraph(attributePaths = {"owner", "vehiclePlateNumber"})
    List<Vehicle> findByVehiclePlateNumber_PlateNumber(String plateNumber);
    Optional<Vehicle> findByVehiclePlateNumber(PlateNumber plateNumber);

}