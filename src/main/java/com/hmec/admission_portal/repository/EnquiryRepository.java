package com.hmec.admission_portal.repository;

import com.hmec.admission_portal.model.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    Optional<Enquiry> findByEnquiryNumber(String enquiryNumber);
}