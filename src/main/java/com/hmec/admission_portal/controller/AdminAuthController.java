// src/main/java/com/hmec/admission_portal/controller/AdminAuthController.java
package com.hmec.admission_portal.controller;

import com.hmec.admission_portal.dto.*;
import com.hmec.admission_portal.model.Admin;
import com.hmec.admission_portal.service.AdminService;
import com.hmec.admission_portal.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AdminRegisterRequest req) {
        Admin admin = adminService.register(req);
        var dto = new AdminResponse(admin.getId(), admin.getUsername(), admin.getEmail(), admin.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequest req) {
        // Manual auth (username + password) since admins are few
        Admin admin = adminService.findByUsername(req.username());
        // Password check is in service? Do here for clarity
        // Better approach: custom UserDetailsService; keeping simple:
        throwIfBadPassword(req.password(), admin.getPasswordHash());

        String token = jwtUtil.generateToken(
                admin.getUsername(),
                Map.of("role", admin.getRole(), "uid", admin.getId())
        );
        var payload = new JwtResponse(token,
                new AdminResponse(admin.getId(), admin.getUsername(), admin.getEmail(), admin.getRole()));
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = (String) auth.getPrincipal();
        Admin admin = adminService.findByUsername(username);
        return ResponseEntity.ok(new AdminResponse(admin.getId(), admin.getUsername(), admin.getEmail(), admin.getRole()));
    }

    // --- helpers ---
    private void throwIfBadPassword(String raw, String hash) {
        // Now this is perfect. It only uses the injected bean.
        if (!passwordEncoder.matches(raw, hash)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
