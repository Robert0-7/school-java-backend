package com.hmec.admission_portal.service;

import com.hmec.admission_portal.dto.AdmissionDTO;
import com.hmec.admission_portal.model.Admission;
import com.hmec.admission_portal.repository.AdmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final FileStorageService fileStorageService;

    public Admission saveAdmission(AdmissionDTO dto) {
        // check if admission already exists
        Admission admission = admissionRepository.findByEnquiryNumber(dto.getEnquiryNumber())
                .orElse(new Admission()); // if not found, create new

        // Always set enquiryNumber and admissionClass
        admission.setEnquiryNumber(dto.getEnquiryNumber());
        admission.setAdmissionClass(dto.getAdmissionClass());

        // Store uploaded images if provided
        if (dto.getChildImage() != null && !dto.getChildImage().isEmpty()) {
            admission.setChildImagePath(fileStorageService.storeFile(dto.getChildImage()));
        }
        if (dto.getFatherImage() != null && !dto.getFatherImage().isEmpty()) {
            admission.setFatherImagePath(fileStorageService.storeFile(dto.getFatherImage()));
        }
        if (dto.getMotherImage() != null && !dto.getMotherImage().isEmpty()) {
            admission.setMotherImagePath(fileStorageService.storeFile(dto.getMotherImage()));
        }

        // map rest of DTO → entity (like you already have in mapDtoToEntity)
        // but here instead of creating new Admission again,
        // just update the existing one
        mapDtoToExistingEntity(dto, admission);

        // Default payment status if new
        if (admission.getPaymentStatus() == null) {
            admission.setPaymentStatus("unpaid");
        }

        // save to DB
        return admissionRepository.save(admission);
    }

    public Admission handlePaymentProofUpload(Long admissionId, String transactionId, MultipartFile paymentProofFile) {
        // 1. Find the admission record, throw exception if not found
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission record not found with id: " + admissionId));

        // 2. Store the payment proof file using your existing service
        String fileName = fileStorageService.storeFile(paymentProofFile);

        // 3. Update the admission entity's status and proof path
        admission.setPaymentStatus("AWAITING_VERIFICATION");
        admission.setPaymentProofPath(fileName);

        // ✅ SET THE TRANSACTION ID
        admission.setTransactionId(transactionId);

        // 4. Save the updated record back to the database
        return admissionRepository.save(admission);
    }

    private void mapDtoToExistingEntity(AdmissionDTO dto, Admission admission) {
        admission.setChildName(dto.getChildName());
        admission.setGender(dto.getGender());
        admission.setDob(dto.getDob_dd() + "/" + dto.getDob_mm() + "/" + dto.getDob_yyyy());
        admission.setBloodGroup(dto.getBloodGroup());
        admission.setNationality(dto.getNationality());
        admission.setReligion(dto.getReligion());
        admission.setCategory(dto.getCategory());
        admission.setState(dto.getState());
        admission.setCaste(dto.getCaste());
        admission.setMotherTongue("Other".equalsIgnoreCase(dto.getMotherTongue())
                ? dto.getMotherTongueOther() : dto.getMotherTongue());
        admission.setSecondLanguage(dto.getSecondLanguage());
        admission.setAddress(dto.getAddress());
        admission.setMobile(dto.getMobile());
        admission.setAadhaar(dto.getAadhaar());

        // father
        admission.setFatherName(dto.getFatherName());
        admission.setFatherQual(dto.getFatherQual());
        admission.setFatherOcc(dto.getFatherOcc());
        admission.setFatherIncome(dto.getFatherIncome());
        admission.setFatherOrg(dto.getFatherOrg());
        admission.setFatherTel(dto.getFatherTel());
        admission.setFatherMobile(dto.getFatherMobile());
        admission.setFatherEmail(dto.getFatherEmail());

        // mother
        admission.setMotherName(dto.getMotherName());
        admission.setMotherQual(dto.getMotherQual());
        admission.setMotherOcc(dto.getMotherOcc());
        admission.setMotherIncome(dto.getMotherIncome());
        admission.setMotherOrg(dto.getMotherOrg());
        admission.setMotherTel(dto.getMotherTel());
        admission.setMotherMobile(dto.getMotherMobile());
        admission.setMotherEmail(dto.getMotherEmail());

        // guardian
        admission.setGuardianRelation(dto.getGuardianRelation());

        // school
        admission.setLastSchool(dto.getLastSchool());
        admission.setLastSchoolAffiliation(dto.getLastSchoolAffiliation());

        // siblings
        admission.setSib1Name(dto.getSib1Name());
        admission.setSib1Id(dto.getSib1Id());
        admission.setSib1Class(dto.getSib1Class());
        admission.setSib2Name(dto.getSib2Name());
        admission.setSib2Id(dto.getSib2Id());
        admission.setSib2Class(dto.getSib2Class());

        // academic
        admission.setIxEng(dto.getIxEng());
        admission.setIxLang(dto.getIxLang());
        admission.setIxMath(dto.getIxMath());
        admission.setIxScience(dto.getIxScience());
        admission.setIxSocial(dto.getIxSst());
        admission.setXEng(dto.getXEng());
        admission.setXLang(dto.getXLang());
        admission.setXMath(dto.getXMath());
        admission.setXScience(dto.getXScience());
        admission.setXSocial(dto.getXSst());

        // group
        admission.setAppliedGroup(dto.getAppliedGroup());
        admission.setChosenSubjects(dto.getChosenSubjects());

        // medical
        admission.setMedicalInfo(dto.getMedicalInfo());
    }

}
