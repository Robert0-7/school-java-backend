package com.hmec.admission_portal.controller;

import com.hmec.admission_portal.model.Admission;
import com.hmec.admission_portal.model.Enquiry;
import com.hmec.admission_portal.repository.AdmissionRepository;
import com.hmec.admission_portal.repository.EnquiryRepository;
import com.hmec.admission_portal.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdmissionRepository admissionRepository;
    private final FileStorageService fileStorageService;
    private final EnquiryRepository enquiryRepository;

    @GetMapping("/enquiries")
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }

    @GetMapping("/admissions")
    public List<Map<String, Object>> getAllAdmissions() {
        return admissionRepository.findAll().stream().map(admission -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", admission.getId());
            dto.put("enquiryNumber", admission.getEnquiryNumber());
            dto.put("admissionClass", admission.getAdmissionClass());
            dto.put("childName", admission.getChildName());
            dto.put("fatherName", admission.getFatherName());
            dto.put("motherName", admission.getMotherName());
            dto.put("paymentStatus", admission.getPaymentStatus());
            dto.put("transactionId", admission.getTransactionId());

            // 🔹 Extra fields for full table
            dto.put("gender", admission.getGender());
            dto.put("dob", admission.getDob());
            dto.put("bloodGroup", admission.getBloodGroup());
            dto.put("nationality", admission.getNationality());
            dto.put("state", admission.getState());
            dto.put("caste", admission.getCaste());
            dto.put("motherTongue", admission.getMotherTongue());
            dto.put("secondLanguage", admission.getSecondLanguage());
            dto.put("address", admission.getAddress());
            dto.put("mobile", admission.getMobile());
            dto.put("aadhaar", admission.getAadhaar());
            dto.put("fatherMobile", admission.getFatherMobile());
            dto.put("fatherEmail", admission.getFatherEmail());
            dto.put("motherMobile", admission.getMotherMobile());
            dto.put("motherEmail", admission.getMotherEmail());
            dto.put("lastSchool", admission.getLastSchool());
            dto.put("lastSchoolAffiliation", admission.getLastSchoolAffiliation());

            // 🔹 Presigned S3 URLs
            if (admission.getChildImagePath() != null) {
                dto.put("childImageUrl", fileStorageService.generatePresignedUrl(admission.getChildImagePath()));
            }
            if (admission.getFatherImagePath() != null) {
                dto.put("fatherImageUrl", fileStorageService.generatePresignedUrl(admission.getFatherImagePath()));
            }
            if (admission.getMotherImagePath() != null) {
                dto.put("motherImageUrl", fileStorageService.generatePresignedUrl(admission.getMotherImagePath()));
            }
            if (admission.getPaymentProofPath() != null) {
                dto.put("paymentProofUrl", fileStorageService.generatePresignedUrl(admission.getPaymentProofPath()));
            }

            return dto;
        }).toList();
    }
}
