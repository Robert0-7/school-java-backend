package com.hmec.admission_portal.dto;

public record AdminResponse(
        Long id,
        String username,
        String email,
        String role
) {}
