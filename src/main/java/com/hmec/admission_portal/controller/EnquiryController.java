package com.hmec.admission_portal.controller;

import com.hmec.admission_portal.dto.EnquiryDTO;
import com.hmec.admission_portal.model.Enquiry;
import com.hmec.admission_portal.service.EnquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<Enquiry> submitEnquiry(@Valid @RequestBody EnquiryDTO enquiryDTO) {
        Enquiry createdEnquiry = enquiryService.createEnquiry(enquiryDTO);
        return new ResponseEntity<>(createdEnquiry, HttpStatus.CREATED);
    }

    @GetMapping("/{enquiryNumber}")
    public ResponseEntity<Enquiry> getEnquiryByNumber(@PathVariable String enquiryNumber) {
        try {
            Enquiry enquiry = enquiryService.findByEnquiryNumber(enquiryNumber);
            return ResponseEntity.ok(enquiry);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * New endpoint to get class info from enquiry number
     * This can be used by frontend to redirect user to the correct form
     */
    @GetMapping("/{enquiryNumber}/class")
    public ResponseEntity<String> getAdmissionClass(@PathVariable String enquiryNumber) {
        try {
            Enquiry enquiry = enquiryService.findByEnquiryNumber(enquiryNumber);
            // Assuming your Enquiry entity has a getAdmissionClass() method
            String admissionClass = enquiry.getAdmissionClass();
            return ResponseEntity.ok(admissionClass);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Enquiry not found for number: " + enquiryNumber);
        }
    }

    @GetMapping(produces = "application/json")
    public List<Enquiry> getAllEnquiries() {
        return enquiryService.findAll();
    }

}
