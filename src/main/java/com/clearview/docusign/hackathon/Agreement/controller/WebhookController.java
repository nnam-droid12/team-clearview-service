package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(WebhookController.class);
    private final AgreementRepository agreementRepository;

    public WebhookController(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    @PostMapping("/docusign")
    public ResponseEntity<String> handleDocuSignWebhook(@RequestBody String payload) {
        try {
            // Parse the webhook payload
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);

            // Extract envelope information
            String envelopeId = rootNode.path("envelopeId").asText();
            String status = rootNode.path("status").asText();

            // Update agreement status
            Agreement agreement = agreementRepository.findByDocuSignEnvelopeId(envelopeId)
                    .orElseThrow(() -> new RuntimeException("Agreement not found"));

            agreement.setStatus(status);
            if ("completed".equalsIgnoreCase(status)) {
                agreement.setSignedDate(LocalDateTime.now());
            }

            agreementRepository.save(agreement);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing webhook: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}