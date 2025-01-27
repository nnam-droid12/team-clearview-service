package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EnvelopDto {
    @JsonProperty("envelopeId")
    private String envelopeId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("emailSubject")
    private String emailSubject;

    @JsonProperty("createdDateTime")
    private String createdDateTime;

    @JsonProperty("sentDateTime")
    private String sentDateTime;

    @JsonProperty("completedDateTime")
    private String completedDateTime;

    @JsonProperty("recipients")
    private List<RecipientDto> recipients;

    @JsonProperty("documents")
    private List<DocumentDto> documents;

    @Data
    public static class RecipientDto {
        private String recipientId;
        private String name;
        private String email;
        private String status;
        private String signedDateTime;
    }

    @Data
    public static class DocumentDto {
        private String documentId;
        private String name;
        private String type;
    }
}