package com.clearview.docusign.hackathon.Agreement.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import com.clearview.docusign.hackathon.Milestone.entities.Milestone;
import com.clearview.docusign.hackathon.Milestone.repository.MilestoneRepository;
import com.clearview.docusign.hackathon.Obligation.entities.Obligation;
import com.clearview.docusign.hackathon.Obligation.repository.ObligationRepository;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
public class DocumentService {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DocumentService.class);
    private final AgreementRepository agreementRepository;
    private final MilestoneRepository milestoneRepository;
    private final ObligationRepository obligationRepository;
    private final TokenService tokenService;

    private final ApiClient apiClient;

    @Value("${docusign.account-id}")
    private String accountId;

    public DocumentService(AgreementRepository agreementRepository, MilestoneRepository milestoneRepository, ObligationRepository obligationRepository, TokenService tokenService, ApiClient apiClient) {
        this.agreementRepository = agreementRepository;
        this.milestoneRepository = milestoneRepository;
        this.obligationRepository = obligationRepository;
        this.tokenService = tokenService;
        this.apiClient = apiClient;
    }

    public Agreement uploadAndInitiateSigningProcess(MultipartFile file, List<String> signerEmails) throws Exception {

        String accessToken = tokenService.getAccessToken();
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");
        envelopeDefinition.setEmailBlurb("Please review and sign this document");

        Document document = new Document();
        document.setDocumentBase64(Base64.getEncoder().encodeToString(file.getBytes()));
        document.setName(file.getOriginalFilename());
        document.setFileExtension(getFileExtension(file.getOriginalFilename()));
        document.setDocumentId("1");

        List<Signer> signerList = new ArrayList<>();
        for (int i = 0; i < signerEmails.size(); i++) {
            Signer signer = new Signer();
            signer.setEmail(signerEmails.get(i));
            signer.setName("Signer " + (i + 1));
            signer.setRecipientId(String.valueOf(i + 1));

            String clientUserId = generateClientUserId(signerEmails.get(i), i + 1);
            signer.setClientUserId(clientUserId);

            SignHere signHere = new SignHere();
            signHere.setDocumentId("1");
            signHere.setPageNumber("1");
            signHere.setRecipientId(String.valueOf(i + 1));
            signHere.setXPosition("100");
            signHere.setYPosition("100");

            DateSigned dateSigned = new DateSigned();
            dateSigned.setDocumentId("1");
            dateSigned.setPageNumber("1");
            dateSigned.setRecipientId(String.valueOf(i + 1));
            dateSigned.setXPosition("200");
            dateSigned.setYPosition("100");

            Tabs tabs = new Tabs();
            tabs.setSignHereTabs(Arrays.asList(signHere));
            tabs.setDateSignedTabs(Arrays.asList(dateSigned));
            signer.setTabs(tabs);

            signerList.add(signer);
        }

        envelopeDefinition.setDocuments(Arrays.asList(document));
        envelopeDefinition.setRecipients(new Recipients().signers(signerList));
        envelopeDefinition.setStatus("sent");

        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelopeDefinition);


        Agreement agreement = Agreement.builder()
                .docuSignEnvelopeId(envelopeSummary.getEnvelopeId())
                .status("SENT")
                .build();

        return agreementRepository.save(agreement);
    }

    public String createEmbeddedSigningUrl(String envelopeId, String signerEmail, String returnUrl) throws Exception {
        String accessToken = tokenService.getAccessToken();
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);

        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        Recipients recipients = envelopesApi.listRecipients(accountId, envelopeId);

        String clientUserId = null;
        String recipientId = null;

        for (Signer signer : recipients.getSigners()) {
            if (signer.getEmail().equals(signerEmail)) {
                recipientId = signer.getRecipientId();
                clientUserId = generateClientUserId(signerEmail, Integer.parseInt(recipientId));
                break;
            }
        }

        if (clientUserId == null) {
            throw new RuntimeException("Signer not found for envelope");
        }

        RecipientViewRequest viewRequest = new RecipientViewRequest();
        viewRequest.setReturnUrl(returnUrl);
        viewRequest.setAuthenticationMethod("email");
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName("Signer " + recipientId);
        viewRequest.setClientUserId(clientUserId);

        ViewUrl results = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
        return results.getUrl();
    }

    private String generateClientUserId(String email, int recipientId) {
        return String.format("%s-%d",
                UUID.nameUUIDFromBytes(email.getBytes()).toString(),
                recipientId
        );
    }


    private String getFileExtension(String filename) {

        return filename.substring(filename.lastIndexOf(".") + 1);
    }


    public void handleSigningCompletion(String envelopeId) {
        Agreement agreement = agreementRepository.findByDocuSignEnvelopeId(envelopeId)
                .orElseThrow(() -> new RuntimeException("Agreement not found for envelope: " + envelopeId));


        agreement.setStatus("SIGNED");
        agreement.setSignedDate(LocalDateTime.now());
        agreementRepository.save(agreement);

        agreement.getMilestones().stream()
                .min(Comparator.comparing(Milestone::getDueDate))
                .ifPresent(milestone -> {
                    milestone.setStatus("IN_PROGRESS");
                    milestoneRepository.save(milestone);
                });

        agreement.getObligations().stream()
                .filter(obligation -> obligation.getDueDate().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Obligation::getDueDate))
                .ifPresent(obligation -> {
                    obligation.setStatus("IN_PROGRESS");
                    obligationRepository.save(obligation);
                });

        log.info("Updated agreement and related entities status after signing completion. AgreementId: {}",
                agreement.getAgreementId());
    }

    public void handleSigningDeclined(String envelopeId) {
        Agreement agreement = agreementRepository.findByDocuSignEnvelopeId(envelopeId)
                .orElseThrow(() -> new RuntimeException("Agreement not found for envelope: " + envelopeId));


        agreement.setStatus("DECLINED");
        agreementRepository.save(agreement);

        agreement.getMilestones().forEach(milestone -> {
            milestone.setStatus("CANCELLED");
            milestoneRepository.save(milestone);
        });

        agreement.getObligations().forEach(obligation -> {
            obligation.setStatus("CANCELLED");
            obligationRepository.save(obligation);
        });

        log.info("Updated agreement and related entities status after signing declined. AgreementId: {}",
                agreement.getAgreementId());
    }
}