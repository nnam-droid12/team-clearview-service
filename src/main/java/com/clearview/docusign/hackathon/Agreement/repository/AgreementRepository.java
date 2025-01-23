package com.clearview.docusign.hackathon.Agreement.repository;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {

        Optional<Agreement> findByDocuSignEnvelopeId(String envelopeId);

        @Query("SELECT a FROM Agreement a WHERE a.docuSignEnvelopeId = :envelopeId " +
                "ORDER BY a.agreementId DESC LIMIT 1")
        Optional<Agreement> findMostRecentByDocuSignEnvelopeId(@Param("envelopeId") String envelopeId);
}
