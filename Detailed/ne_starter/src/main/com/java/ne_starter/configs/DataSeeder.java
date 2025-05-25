package com.java.ne_starter.configs;

import com.java.ne_starter.enumerations.user.EUserRole;
import com.java.ne_starter.models.Role;
import com.java.ne_starter.models.User;
import com.java.ne_starter.repositories.RoleRepository;
import com.java.ne_starter.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j // Enables logging
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedRoles();
        seedDefaultAdmin();
    }

    private void seedRoles() {
        for (EUserRole role : EUserRole.values()) {
            roleRepository.findByName(role)
                    .orElseGet(() -> {
                        Role newRole = new Role(role);
                        roleRepository.save(newRole);
                        log.info("✅ Role '{}' created and saved.", role.name());
                        return newRole;
                    });
        }
    }

    private void seedDefaultAdmin() {
        String adminEmail = "admin@ne-starter.com";

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("ℹ️ Admin already exists. Skipping seeding for admin.");
            return;
        }

        User admin = new User();
        admin.setFirstName("System");
        admin.setLastName("Admin");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("Admin1234!"));
        admin.setPhoneNumber("0780000000");
        admin.setNationalId("1234567890123456");
        admin.setEmailVerified(true);

        Role adminRole = roleRepository.findByName(EUserRole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found."));

        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        log.info("✅ Admin user seeded with email '{}'.", adminEmail);
    }
}
