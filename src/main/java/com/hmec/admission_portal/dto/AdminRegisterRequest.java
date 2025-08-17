// src/main/java/com/hmec/admission_portal/dto/AdminRegisterRequest.java
package com.hmec.admission_portal.dto;

import jakarta.validation.constraints.*;

public record AdminRegisterRequest(
        @NotBlank @Size(min=3, max=60) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min=8, max=72) String password
) {}
