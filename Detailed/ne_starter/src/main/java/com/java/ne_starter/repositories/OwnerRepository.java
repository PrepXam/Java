package com.java.ne_starter.repositories;

import com.java.ne_starter.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    Optional<Owner> findByNationalId(String nationalId);
    Optional<Owner> findByPhoneNumber(String phoneNumber);
    Optional<Owner> findByEmail(String email);

    boolean existsByNationalId(String nationalId);
}