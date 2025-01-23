package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.service.AgreementService;
import com.clearview.docusign.hackathon.auth.config.DocuSignConfig;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DocuSignConfig.class);

    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/sign-agreement")
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.createAgreement(agreement));
    }

    @GetMapping("/get-agreementById/{id}")
    public ResponseEntity<Agreement> getAgreement(@PathVariable("id") Long agreementId) {
        return ResponseEntity.ok(agreementService.getAgreement(agreementId));
    }

    @GetMapping("/get-all-agreement")
    public ResponseEntity<Page<Agreement>> getAllAgreements(Pageable pageable) {
        return ResponseEntity.ok(agreementService.getAllAgreements(pageable));
    }

    @PutMapping("/update-agreement/{id}")
    public ResponseEntity<Agreement> updateAgreement(
            @PathVariable("id") Long agreementId,
            @RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.updateAgreement(agreementId, agreement));
    }

    @PatchMapping("/update-status/{id}/status")
    public ResponseEntity<Agreement> updateStatus(
            @PathVariable("id") Long agreementId,
            @RequestBody String status) {
        return ResponseEntity.ok(agreementService.updateAgreementStatus(agreementId, status));
    }

    @DeleteMapping("/delete-agreement/{id}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable("id") Long agreementId) {
        agreementService.deleteAgreement(agreementId);
        return ResponseEntity.noContent().build();
    }


}