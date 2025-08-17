package com.hmec.admission_portal.dto;

public record JwtResponse(
        String token,
        AdminResponse admin
) {}
