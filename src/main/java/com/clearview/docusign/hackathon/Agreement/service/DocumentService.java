package com.clearview.docusign.hackathon.Agreement.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class DocumentService {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DocumentService.class);
    private final AgreementRepository agreementRepository;

    private final ApiClient apiClient;

    @Value("${docusign.account-id}")
    private String accountId;

    public DocumentService(AgreementRepository agreementRepository, ApiClient apiClient) {
        this.agreementRepository = agreementRepository;
        this.apiClient = apiClient;
    }

    public Agreement uploadAndInitiateSigningProcess(MultipartFile file, List<String> signerEmails) throws IOException, ApiException {
        // Create envelope definition
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");

        // Convert file to Base64
        String base64Doc = Base64.getEncoder().encodeToString(file.getBytes());

        // Create document
        Document document = new Document();
        document.setDocumentBase64(base64Doc);
        document.setName(file.getOriginalFilename());
        document.setFileExtension(getFileExtension(file.getOriginalFilename()));
        document.setDocumentId("1");

        // Create signers
        List<Signer> signerList = new ArrayList<>();
        for (int i = 0; i < signerEmails.size(); i++) {
            Signer signer = new Signer();
            signer.setEmail(signerEmails.get(i));
            signer.setName("Signer " + (i + 1));
            signer.setRecipientId(String.valueOf(i + 1));

            SignHere signHere = new SignHere();
            signHere.setDocumentId("1");
            signHere.setPageNumber("1");
            signHere.setRecipientId(String.valueOf(i + 1));
            signHere.setXPosition("100");
            signHere.setYPosition("100");

            List<SignHere> signHereList = Arrays.asList(signHere);
            Tabs tabs = new Tabs();
            tabs.setSignHereTabs(signHereList);
            signer.setTabs(tabs);

            signerList.add(signer);
        }

        // Set envelope properties
        envelopeDefinition.setDocuments(Arrays.asList(document));
        envelopeDefinition.setRecipients(new Recipients().signers(signerList));
        envelopeDefinition.setStatus("sent");

        // Create and send envelope
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelopeDefinition);

        // Create and save agreement
        Agreement agreement = Agreement.builder()
                .docuSignEnvelopeId(envelopeSummary.getEnvelopeId())
                .status("SENT")
                .build();

        return agreementRepository.save(agreement);
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}