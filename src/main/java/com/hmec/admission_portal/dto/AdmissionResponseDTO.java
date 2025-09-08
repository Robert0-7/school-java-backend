package com.hmec.admission_portal.dto;

import lombok.Data;

@Data
public class AdmissionResponseDTO {
    private Long id;
    private String enquiryNumber;
    private String admissionClass;
    private String childName;
    private String fatherName;
    private String motherName;

    private String paymentStatus;
    private String transactionId;

    // These will be presigned URLs instead of raw S3 keys
    private String childImageUrl;
    private String fatherImageUrl;
    private String motherImageUrl;
    private String paymentProofUrl;
}
