package com.hmec.admission_portal.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class AdmissionDTO {

    // From Enquiry
    @NotBlank(message = "Enquiry number is required")
    private String enquiryNumber;

    @NotBlank(message = "Admission class is required")
    private String admissionClass;

    // --- Child's Details ---
    @NotBlank(message = "Child's name cannot be empty")
    @Size(max = 100, message = "Child's name is too long")
    private String childName;

    @NotBlank(message = "Gender must be selected")
    private String gender;

    @NotBlank(message = "Day of birth is required")
    private String dob_dd;
    @NotBlank(message = "Month of birth is required")
    private String dob_mm;
    @NotBlank(message = "Year of birth is required")
    private String dob_yyyy;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    // Additional personal info
    private String nationality;
    private String religion;

    // For Class XI category selection
    private String category; // Backward, Most backward, SC, ST, Others
    private String state;    // State to which the child belongs

    @NotBlank(message = "Caste is required")
    private String caste;

    @NotBlank(message = "Mother tongue is required")
    private String motherTongue;
    private String motherTongueOther;

    private String secondLanguage; // For Nursery-IX

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String mobile;

    private String aadhaar;

    // --- Father’s Details ---
    @NotBlank(message = "Father's name is required")
    private String fatherName;

    private String fatherQual;
    private String fatherOcc;
    private String fatherIncome;
    private String fatherOrg;
    private String fatherTel;

    @NotBlank(message = "Father's mobile number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String fatherMobile;

    @Email(message = "Please provide a valid email for the father")
    private String fatherEmail;

    // --- Mother’s Details ---
    @NotBlank(message = "Mother's name is required")
    private String motherName;

    private String motherQual;
    private String motherOcc;
    private String motherIncome;
    private String motherOrg;
    private String motherTel;

    @NotBlank(message = "Mother's mobile number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String motherMobile;

    @Email(message = "Please provide a valid email for the mother")
    private String motherEmail;

    // --- Guardian ---
    private String guardianRelation; // Step Son/Daughter/Adopted/Relative

    // --- Previous School ---
    private String lastSchool;
    private List<String> lastSchoolAffiliation; // CBSE / ICSE / WBBSE / Others

    // --- Siblings ---
    private String sib1Name;
    private String sib1Id;
    private String sib1Class;
    private String sib2Name;
    private String sib2Id;
    private String sib2Class;

    // --- Performance Record (Class XI) ---
    @Min(0) @Max(100)
    private Integer ixEng;
    @Min(0) @Max(100)
    private Integer ixLang;
    @Min(0) @Max(100)
    private Integer ixMath;
    @Min(0) @Max(100)
    private Integer ixScience;
    @Min(0) @Max(100)
    private Integer ixSst;

    @Min(0) @Max(100)
    private Integer xEng;
    @Min(0) @Max(100)
    private Integer xLang;
    @Min(0) @Max(100)
    private Integer xMath;
    @Min(0) @Max(100)
    private Integer xScience;
    @Min(0) @Max(100)
    private Integer xSst;

    // --- Class XI Specific ---
    private String appliedGroup; // Group A, B, or C
    private List<String> chosenSubjects; // Selected subjects at +2 level

    // --- Medical Info ---
    private String medicalInfo;

    // --- Transport ---
    private String transportRequired; // Yes/No

    // --- Uploaded Files ---
    @NotNull(message = "Child's image is required")
    private MultipartFile childImage;

    @NotNull(message = "Father's image is required")
    private MultipartFile fatherImage;

    @NotNull(message = "Mother's image is required")
    private MultipartFile motherImage;
}
