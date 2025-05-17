package com.java.ne_starter.repositories;


import com.java.ne_starter.models.Role;
import com.java.ne_starter.enumerations.user.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(EUserRole name);
}