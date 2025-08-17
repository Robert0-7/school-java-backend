package com.hmec.admission_portal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // From Enquiry
    private String enquiryNumber;
    private String admissionClass;

    // Child’s Details
    private String childName;
    private String gender;
    private String dob; // Combined dd/mm/yyyy
    private String bloodGroup;
    private String nationality; // Class XI only
    private String religion;    // Nursery–IX only
    private String category;    // Class XI only: Backward, Most backward, SC, ST, Others
    private String state;       // Class XI only
    private String caste;       // Nursery–IX
    private String motherTongue;
    private String secondLanguage; // Nursery–IX only: Hindi/Bengali
    private String address;
    private String mobile;
    private String aadhaar;

    // Father’s Details
    private String fatherName;
    private String fatherQual;
    private String fatherOcc;
    private String fatherIncome;
    private String fatherOrg;
    private String fatherTel;
    private String fatherMobile;
    private String fatherEmail;

    // Mother’s Details
    private String motherName;
    private String motherQual;
    private String motherOcc;
    private String motherIncome;
    private String motherOrg;
    private String motherTel;
    private String motherMobile;
    private String motherEmail;

    // Guardian
    private String guardianRelation; // Step son, Step daughter, Adopted, Relative

    // School Details
    private String lastSchool;
    @ElementCollection
    private List<String> lastSchoolAffiliation; // CBSE, ICSE, etc.

    // Sibling details
    private String sib1Name;
    private String sib1Id;
    private String sib1Class;
    private String sib2Name;
    private String sib2Id;
    private String sib2Class;

    // Alumni & Staff Info (Nursery–IX)
    private Boolean isParentAlumni;
    private String alumniSchool;
    private String alumniCertPath; // optional if storing file
    private String staffName;
    private String staffDesignation;

    // Transport
    private Boolean wantsTransport; // Nursery–IX

    // Academic Performance (Class XI)
    private Integer ixEng;
    private Integer ixLang;
    private Integer ixMath;
    private Integer ixScience;
    private Integer ixSocial;
    private Integer xEng;
    private Integer xLang;
    private Integer xMath;
    private Integer xScience;
    private Integer xSocial;

    // Group & Subjects (Class XI)
    private String appliedGroup; // A, B, C
    @ElementCollection
    @CollectionTable(name = "chosen_subjects", joinColumns = @JoinColumn(name = "admission_id"))
    @Column(name = "subject")
    private List<String> chosenSubjects;

    // Medical Info
    @Column(length = 1000)
    private String medicalInfo;

    // File paths for images
    private String childImagePath;
    private String fatherImagePath;
    private String motherImagePath;

    // Payment / Status
    private String paymentStatus = "unpaid";
    private String paymentProofPath;
    private String transactionId;
}
