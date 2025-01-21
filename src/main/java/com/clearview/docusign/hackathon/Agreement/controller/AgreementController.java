package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.service.AgreementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.createAgreement(agreement));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreement(@PathVariable("id") Long agreementId) {
        return ResponseEntity.ok(agreementService.getAgreement(agreementId));
    }

    @GetMapping
    public ResponseEntity<Page<Agreement>> getAllAgreements(Pageable pageable) {
        return ResponseEntity.ok(agreementService.getAllAgreements(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agreement> updateAgreement(
            @PathVariable("id") Long agreementId,
            @RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.updateAgreement(agreementId, agreement));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Agreement> updateStatus(
            @PathVariable("id") Long agreementId,
            @RequestBody String status) {
        return ResponseEntity.ok(agreementService.updateAgreementStatus(agreementId, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable("id") Long agreementId) {
        agreementService.deleteAgreement(agreementId);
        return ResponseEntity.noContent().build();
    }
}