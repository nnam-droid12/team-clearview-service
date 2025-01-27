package com.clearview.docusign.hackathon.NavigatorAgreement.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Milestone.entities.Milestone;
import com.clearview.docusign.hackathon.NavigatorAgreement.utils.EnvelopDto;
import com.clearview.docusign.hackathon.Obligation.entities.Obligation;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgreementMapperService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AgreementMapperService.class);
    public Agreement mapToEntity(EnvelopDto dto) {
        log.info("Starting mapping with DTO: {}", dto);
        if (dto == null) {
            log.warn("Received null EnvelopDto");
            return null;
        }

        Agreement agreement = new Agreement();

        log.info("Mapping envelopeId: {}", dto.getEnvelopeId());
        agreement.setDocuSignEnvelopeId(dto.getEnvelopeId());

        log.info("Mapping emailSubject: {}", dto.getEmailSubject());
        agreement.setTitle(dto.getEmailSubject());

        log.info("Mapping status from: {}", dto.getStatus());
        agreement.setStatus(mapStatus(dto.getStatus()));


        if (dto.getCompletedDateTime() != null) {
            agreement.setSignedDate(parseDateTime(dto.getCompletedDateTime()));
        }

        // Map milestones
        List<Milestone> milestones = new ArrayList<>();

        addMilestoneIfPresent(milestones, agreement,
                dto.getCreatedDateTime(), "Created");
        addMilestoneIfPresent(milestones, agreement,
                dto.getSentDateTime(), "Sent");
        addMilestoneIfPresent(milestones, agreement,
                dto.getCompletedDateTime(), "Completed");

        agreement.setMilestones(milestones);

        // Map obligations (from recipients)
        if (dto.getRecipients() != null) {
            List<Obligation> obligations = new ArrayList<>();

            for (EnvelopDto.RecipientDto recipient : dto.getRecipients()) {
                addObligationIfPresent(obligations, agreement,
                        "Signature Required: " + recipient.getName(),
                        "Status: " + recipient.getStatus());
            }

            agreement.setObligations(obligations);
        }

        return agreement;
    }

    private String mapStatus(String docuSignStatus) {
        if (docuSignStatus == null) return "UNKNOWN";

        return switch (docuSignStatus.toUpperCase()) {
            case "COMPLETED" -> "SIGNED";
            case "SENT" -> "PENDING";
            case "DELIVERED" -> "PENDING_SIGNATURE";
            default -> docuSignStatus;
        };
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null) return null;
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    private void addMilestoneIfPresent(List<Milestone> milestones, Agreement agreement,
                                       String dateStr, String description) {
        if (dateStr != null) {
            Milestone milestone = new Milestone();
            milestone.setAgreement(agreement);
            milestone.setDueDate(parseDateTime(dateStr));
            milestone.setDescription(description);
            milestones.add(milestone);
        }
    }

    private void addObligationIfPresent(List<Obligation> obligations, Agreement agreement,
                                        String title, String description) {
        Obligation obligation = new Obligation();
        obligation.setAgreement(agreement);
        obligation.setDescription(description);
        obligations.add(obligation);
    }
}