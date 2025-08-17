// src/main/java/com/hmec/admission_portal/config/BootstrapAdmin.java
package com.hmec.admission_portal.config;

import com.hmec.admission_portal.model.Admin;
import com.hmec.admission_portal.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BootstrapAdmin {
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seedAdmin(AdminRepository repo) {
        return args -> {
            if (!repo.existsByUsername("superadmin")) {
                Admin a = Admin.builder()
                        .username("superadmin")
                        .email("admin@hmec.in")
                        .passwordHash(encoder.encode("StrongPass#2025"))
                        .role("ADMIN")
                        .enabled(true)
                        .build();
                repo.save(a);
            }
        };
    }
}
