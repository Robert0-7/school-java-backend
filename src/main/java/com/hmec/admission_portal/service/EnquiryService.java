package com.hmec.admission_portal.service;

import com.hmec.admission_portal.dto.EnquiryDTO;
import com.hmec.admission_portal.model.Enquiry;
import com.hmec.admission_portal.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    @Transactional
    public Enquiry createEnquiry(EnquiryDTO dto) {
        Enquiry enquiry = new Enquiry();

        // Map all fields from DTO to the entity
        enquiry.setName(dto.getName());
        enquiry.setGender(dto.getGender());
        enquiry.setFatherName(dto.getFatherName());
        enquiry.setFatherProfession(dto.getFatherProfession());
        enquiry.setFatherIncome(dto.getFatherIncome());
        enquiry.setMotherName(dto.getMotherName());
        enquiry.setMotherProfession(dto.getMotherProfession());
        enquiry.setMotherIncome(dto.getMotherIncome());
        enquiry.setAdmissionClass(dto.getAdmissionClass());
        enquiry.setDob(dto.getDob());
        enquiry.setContact1(dto.getContact1());
        enquiry.setEmail(dto.getEmail());
        enquiry.setAddress(dto.getAddress());
        enquiry.setQuery(dto.getQuery());
        enquiry.setSource(dto.getSource());

        // Generate a unique enquiry number
        enquiry.setEnquiryNumber(generateUniqueEnquiryNumber());

        return enquiryRepository.save(enquiry);
    }

    public Enquiry findByEnquiryNumber(String enquiryNumber) {
        return enquiryRepository.findByEnquiryNumber(enquiryNumber)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with number: " + enquiryNumber));
    }

    public List<Enquiry> findAll() {
        return enquiryRepository.findAll();
    }

    private String generateUniqueEnquiryNumber() {
        // Example: ENQ-1660502400-8435
        long timestamp = Instant.now().getEpochSecond();
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "ENQ-" + timestamp + "-" + randomNum;
    }

}