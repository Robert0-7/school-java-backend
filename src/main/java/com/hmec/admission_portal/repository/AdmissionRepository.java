package com.hmec.admission_portal.repository;

import com.hmec.admission_portal.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    Optional<Admission> findByEnquiryNumber(String enquiryNumber);
}
