package com.hmec.admission_portal.dto;

import jakarta.validation.constraints.*;

public record AdminLoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
