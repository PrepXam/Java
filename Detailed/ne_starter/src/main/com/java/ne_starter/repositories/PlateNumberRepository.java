package com.java.ne_starter.repositories;

import com.java.ne_starter.enumerations.plate.PlateStatus;
import com.java.ne_starter.models.PlateNumber;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlateNumberRepository extends JpaRepository<PlateNumber, UUID> {
    List<PlateNumber> findByOwnerId(UUID ownerId);
    Optional<PlateNumber> findByPlateNumber(String plateNumber);
    Optional<PlateNumber> findByIdAndOwnerId(UUID plateId, UUID ownerId);

    @EntityGraph(attributePaths = {"owner"})
    List<PlateNumber> findByOwnerIdAndStatus(UUID ownerId, PlateStatus status);
}