package com.hmec.admission_portal.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class EnquiryDTO {

    @NotBlank(message = "Child's name is required")
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Father's name is required")
    private String fatherName;

    @NotBlank(message = "Father's profession is required")
    private String fatherProfession;

    @NotNull(message = "Father's income is required")
    @PositiveOrZero(message = "Income cannot be negative")
    private BigDecimal fatherIncome;

    @NotBlank(message = "Mother's name is required")
    private String motherName;

    @NotBlank(message = "Mother's profession is required")
    private String motherProfession;

    @NotNull(message = "Mother's income is required")
    @PositiveOrZero(message = "Income cannot be negative")
    private BigDecimal motherIncome;

    @NotBlank(message = "Admission class is required")
    private String admissionClass;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotBlank(message = "At least one contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
    private String contact1;

    @NotBlank(message = "Email ID is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    private String query;

    // For the checkboxes. We can use @NotEmpty if at least one is required.
    private List<String> source;
}