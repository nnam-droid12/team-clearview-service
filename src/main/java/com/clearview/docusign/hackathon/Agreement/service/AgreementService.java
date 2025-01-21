package com.clearview.docusign.hackathon.Agreement.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AgreementService {

    private final AgreementRepository agreementRepository;

    public AgreementService(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    public Agreement createAgreement(Agreement agreement) {
        agreement.setStatus("DRAFT");
        return agreementRepository.save(agreement);
    }

    public Agreement updateAgreement(Long agreementId, Agreement updatedAgreement) {
        Agreement existingAgreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new EntityNotFoundException("Agreement not found with id: " + agreementId));

        if (updatedAgreement.getStatus() != null) {
            existingAgreement.setStatus(updatedAgreement.getStatus());
        }
        if (updatedAgreement.getSignedDate() != null) {
            existingAgreement.setSignedDate(updatedAgreement.getSignedDate());
        }
        if (updatedAgreement.getDocuSignEnvelopeId() != null) {
            existingAgreement.setDocuSignEnvelopeId(updatedAgreement.getDocuSignEnvelopeId());
        }

        return agreementRepository.save(existingAgreement);
    }

    public Agreement getAgreement(Long agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> new EntityNotFoundException("Agreement not found with id: " + agreementId));
    }

    public Page<Agreement> getAllAgreements(Pageable pageable) {
        return agreementRepository.findAll(pageable);
    }

    public Agreement updateAgreementStatus(Long agreementId, String status) {
        Agreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new EntityNotFoundException("Agreement not found with id: " + agreementId));

        agreement.setStatus(status);
        if ("COMPLETED".equalsIgnoreCase(status)) {
            agreement.setSignedDate(LocalDateTime.now());
        }

        return agreementRepository.save(agreement);
    }

    public Agreement updateDocuSignStatus(String envelopeId, String status) {
        Agreement agreement = agreementRepository.findByDocuSignEnvelopeId(envelopeId)
                .orElseThrow(() -> new EntityNotFoundException("Agreement not found with envelopeId: " + envelopeId));

        agreement.setStatus(status);
        if ("completed".equalsIgnoreCase(status)) {
            agreement.setSignedDate(LocalDateTime.now());
        }

        return agreementRepository.save(agreement);
    }

    public void deleteAgreement(Long agreementId) {
        agreementRepository.deleteById(agreementId);
    }
}