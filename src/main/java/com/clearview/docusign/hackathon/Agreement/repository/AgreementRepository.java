package com.clearview.docusign.hackathon.Agreement.repository;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {

        Optional<Agreement> findByDocuSignEnvelopeId(String envelopeId);
}
