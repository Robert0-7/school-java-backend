package com.hmec.admission_portal.controller;

import com.hmec.admission_portal.dto.AdmissionDTO;
import com.hmec.admission_portal.model.Admission;
import com.hmec.admission_portal.repository.AdmissionRepository;
import com.hmec.admission_portal.repository.EnquiryRepository;
import com.hmec.admission_portal.service.AdmissionService;
import com.hmec.admission_portal.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class AdmissionController {

    private final EnquiryRepository enquiryRepository;
    private final AdmissionRepository admissionRepository;
    private final AdmissionService admissionService;
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<String> createAdmission(@Valid @ModelAttribute AdmissionDTO admissionDTO) {
        var enquiryOpt = enquiryRepository.findByEnquiryNumber(admissionDTO.getEnquiryNumber());
        if (enquiryOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Invalid enquiry number\"}");
        }

        String expectedClass = enquiryOpt.get().getAdmissionClass();
        if (!expectedClass.equalsIgnoreCase(admissionDTO.getAdmissionClass())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Admission class does not match enquiry record\"}");
        }

        Admission savedAdmission = admissionService.saveAdmission(admissionDTO);

        String jsonResponse = "{\"id\":" + savedAdmission.getId() + "}";
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }

    @PostMapping("/by-enquiry/{enquiryNumber}/upload-payment-proof")
    public ResponseEntity<?> uploadPaymentProofByEnquiry(
            @PathVariable String enquiryNumber,
            @RequestParam("transactionId") String transactionId,
            @RequestParam("paymentProof") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Please select a file to upload."));
        }

        return admissionRepository.findByEnquiryNumber(enquiryNumber)
                .map(admission -> {
                    try {
                        Admission updated = admissionService.handlePaymentProofUpload(
                                admission.getId(), transactionId, file);

                        return ResponseEntity.ok(Map.of(
                                "message", "Payment proof uploaded successfully",
                                "admissionId", updated.getId(),
                                "enquiryNumber", updated.getEnquiryNumber(),
                                "paymentStatus", updated.getPaymentStatus()
                        ));
                    } catch (Exception e) {
                        log.error("❌ Error uploading payment proof for enquiry {}: {}", enquiryNumber, e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Could not upload the file: " + e.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No admission found for enquiry number: " + enquiryNumber)));
    }

    @PostMapping("/{admissionId}/upload-payment-proof")
    public ResponseEntity<?> uploadPaymentProofById(
            @PathVariable Long admissionId,
            @RequestParam("transactionId") String transactionId,
            @RequestParam("paymentProof") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Please select a file to upload."));
        }

        try {
            Admission updated = admissionService.handlePaymentProofUpload(admissionId, transactionId, file);
            return ResponseEntity.ok(Map.of(
                    "message", "Payment proof uploaded successfully",
                    "admissionId", updated.getId(),
                    "enquiryNumber", updated.getEnquiryNumber(),
                    "paymentStatus", updated.getPaymentStatus()
            ));
        } catch (Exception e) {
            log.error("❌ Error uploading payment proof for admissionId {}: {}", admissionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not upload the file: " + e.getMessage()));
        }
    }

    // GET /api/admissions - return all admissions as JSON
    @GetMapping(produces = "application/json")
    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename,
            HttpServletRequest request) {

        Resource resource = fileStorageService.loadFileAsResource(filename);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.warn("⚠️ Could not determine file type for {}. Defaulting to application/octet-stream", filename);
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
