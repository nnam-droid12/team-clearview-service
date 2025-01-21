package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.service.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
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
            // Convert JSON array to List<String>
            List<String> signerEmails = new ObjectMapper().readValue(signerEmailsJson, new TypeReference<List<String>>() {});
            Agreement agreement = documentService.uploadAndInitiateSigningProcess(file, signerEmails);
            return ResponseEntity.ok(agreement);
        } catch (Exception e) {
            log.error("Error processing document: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}