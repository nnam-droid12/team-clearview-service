package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import com.clearview.docusign.hackathon.Agreement.service.AgreementService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final AgreementService agreementService;

    public WebhookController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/docusign")
    public ResponseEntity<String> handleDocuSignWebhook(@RequestBody Map<String, Object> payload) {
        log.info("Received DocuSign webhook: {}", payload);

        try {
            Map<String, Object> envelopeStatus = (Map<String, Object>) payload.get("envelopeStatus");
            if (envelopeStatus == null) {
                log.error("Invalid webhook payload: missing envelopeStatus");
                return ResponseEntity.badRequest().body("Invalid webhook payload");
            }

            String envelopeId = (String) envelopeStatus.get("envelopeId");
            String status = (String) envelopeStatus.get("status");

            if (envelopeId == null || status == null) {
                log.error("Invalid webhook payload: missing required fields");
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            String applicationStatus;
            LocalDateTime signedDate = null;

            switch (status.toLowerCase()) {
                case "completed":
                    applicationStatus = "SIGNED";
                    signedDate = LocalDateTime.now();
                    break;
                case "declined":
                    applicationStatus = "DECLINED";
                    break;
                case "sent":
                    applicationStatus = "SENT";
                    break;
                default:
                    applicationStatus = "PENDING";
            }

            agreementService.findByEnvelopeIdAndUpdateStatus(envelopeId, applicationStatus, signedDate);

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            log.error("Error processing webhook: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}