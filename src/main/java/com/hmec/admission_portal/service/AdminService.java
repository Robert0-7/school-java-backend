package com.hmec.admission_portal.service;

import com.hmec.admission_portal.dto.AdminRegisterRequest;
import com.hmec.admission_portal.model.Admin;
import com.hmec.admission_portal.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Admin register(AdminRegisterRequest req) {
        if (adminRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (adminRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Admin admin = Admin.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role("ADMIN")
                .enabled(true)
                .build();
        return adminRepository.save(admin);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
    }
}
