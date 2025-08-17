package com.hmec.admission_portal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enquiry_number", nullable = false, unique = true)
    private String enquiryNumber;

    private String name;
    private String gender;
    private String fatherName;
    private String fatherProfession;
    private BigDecimal fatherIncome;
    private String motherName;
    private String motherProfession;
    private BigDecimal motherIncome;
    private String admissionClass;
    private LocalDate dob;
    private String contact1;
    private String email;
    private String address;
    private String query;

    @ElementCollection(fetch = FetchType.EAGER) // Store the list of sources
    @CollectionTable(name = "enquiry_sources", joinColumns = @JoinColumn(name = "enquiry_id"))
    @Column(name = "source")
    private List<String> source;
}