// src/main/java/com/hmec/admission_portal/controller/AdminController.java
package com.hmec.admission_portal.controller;

import com.hmec.admission_portal.model.Admission;
import com.hmec.admission_portal.model.Enquiry;
import com.hmec.admission_portal.repository.AdmissionRepository;
import com.hmec.admission_portal.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdmissionRepository admissionRepository;
    private final EnquiryRepository enquiryRepository;

    /**
     * Get all enquiries
     * Endpoint: GET /api/admin/enquiries
     */
    @GetMapping("/enquiries")
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {
        return ResponseEntity.ok(enquiryRepository.findAll());
    }

    /**
     * Get all admissions
     * Endpoint: GET /api/admin/admissions
     */
    @GetMapping("/admissions")
    public ResponseEntity<List<Admission>> getAllAdmissions() {
        return ResponseEntity.ok(admissionRepository.findAll());
    }

    /**
     * Get details of a single admission by ID
     * Endpoint: GET /api/admin/admissions/{id}
     */
    @GetMapping("/admissions/{id}")
    public ResponseEntity<Admission> getAdmissionById(@PathVariable Long id) {
        return admissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
