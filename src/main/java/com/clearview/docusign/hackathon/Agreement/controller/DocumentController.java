package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.service.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DocumentController.class);
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Agreement> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("signerEmails") String signerEmailsJson) {
        try {
            List<String> signerEmails = new ObjectMapper().readValue(signerEmailsJson, new TypeReference<List<String>>() {});
            Agreement agreement = documentService.uploadAndInitiateSigningProcess(file, signerEmails);
            return ResponseEntity.ok(agreement);
        } catch (Exception e) {
            log.error("Error processing document: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/signing-url")
    public ResponseEntity<Map<String, String>> getSigningUrl(
            @RequestParam String envelopeId,
            @RequestParam String signerEmail,
            @RequestParam String returnUrl) {
        try {
            String signingUrl = documentService.createEmbeddedSigningUrl(
                    envelopeId,
                    signerEmail,
                    returnUrl
            );

            return ResponseEntity.ok(Map.of("signingUrl", signingUrl));
        } catch (Exception e) {
            log.error("Error getting signing URL: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/signing-complete")
    public ResponseEntity<String> handleSigningCompletion(
            @RequestParam("event") String event,
            @RequestParam("envelopeId") String envelopeId) {

        log.info("Received signing completion callback with event: {} for envelope: {}", event, envelopeId);

        try {
            switch (event) {
                case "signing_complete":
                    documentService.handleSigningCompletion(envelopeId);
                    return ResponseEntity.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body("""
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <title>Signing Complete</title>
                                <style>
                                    body { 
                                        font-family: Arial, sans-serif;
                                        display: flex;
                                        justify-content: center;
                                        align-items: center;
                                        height: 100vh;
                                        margin: 0;
                                        background-color: #f5f5f5;
                                    }
                                    .container {
                                        text-align: center;
                                        padding: 2rem;
                                        background-color: white;
                                        border-radius: 8px;
                                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                    }
                                    .success-icon {
                                        color: #4CAF50;
                                        font-size: 48px;
                                        margin-bottom: 1rem;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="container">
                                    <div class="success-icon">✓</div>
                                    <h1>Thank You!</h1>
                                    <p>The document has been successfully signed.</p>
                                    <p>You can now close this window.</p>
                                </div>
                            </body>
                            </html>
                            """);

                case "signing_declined":
                    documentService.handleSigningDeclined(envelopeId);
                    return ResponseEntity.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body("""
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <title>Signing Declined</title>
                                <style>
                                    body { 
                                        font-family: Arial, sans-serif;
                                        display: flex;
                                        justify-content: center;
                                        align-items: center;
                                        height: 100vh;
                                        margin: 0;
                                        background-color: #f5f5f5;
                                    }
                                    .container {
                                        text-align: center;
                                        padding: 2rem;
                                        background-color: white;
                                        border-radius: 8px;
                                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                    }
                                    .declined-icon {
                                        color: #f44336;
                                        font-size: 48px;
                                        margin-bottom: 1rem;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="container">
                                    <div class="declined-icon">✕</div>
                                    <h1>Signing Declined</h1>
                                    <p>You have chosen not to sign the document.</p>
                                    <p>You can now close this window.</p>
                                </div>
                            </body>
                            </html>
                            """);

                default:
                    log.warn("Unknown signing event received: {}", event);
                    return ResponseEntity.badRequest()
                            .contentType(MediaType.TEXT_HTML)
                            .body("""
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <title>Unknown Status</title>
                                <style>
                                    body { 
                                        font-family: Arial, sans-serif;
                                        display: flex;
                                        justify-content: center;
                                        align-items: center;
                                        height: 100vh;
                                        margin: 0;
                                        background-color: #f5f5f5;
                                    }
                                    .container {
                                        text-align: center;
                                        padding: 2rem;
                                        background-color: white;
                                        border-radius: 8px;
                                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                    }
                                    .unknown-icon {
                                        color: #ff9800;
                                        font-size: 48px;
                                        margin-bottom: 1rem;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="container">
                                    <div class="unknown-icon">?</div>
                                    <h1>Unknown Status</h1>
                                    <p>An unknown status was received.</p>
                                    <p>You can now close this window.</p>
                                </div>
                            </body>
                            </html>
                            """);
            }
        } catch (Exception e) {
            log.error("Error handling signing completion", e);
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_HTML)
                    .body("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Error</title>
                        <style>
                            body { 
                                font-family: Arial, sans-serif;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                                margin: 0;
                                background-color: #f5f5f5;
                            }
                            .container {
                                text-align: center;
                                padding: 2rem;
                                background-color: white;
                                border-radius: 8px;
                                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            }
                            .error-icon {
                                color: #f44336;
                                font-size: 48px;
                                margin-bottom: 1rem;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="error-icon">!</div>
                            <h1>Error</h1>
                            <p>An error occurred while processing your request.</p>
                            <p>You can now close this window.</p>
                        </div>
                    </body>
                    </html>
                    """);
        }
    }

}