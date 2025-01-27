package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import lombok.Data;

import java.util.List;

@Data
public class AgreementNavigatorDto {
        private String id;
        private String type;
        private String category;
        private List<Party> parties;
        private Provisions provisions;
        private RelatedAgreementDocuments relatedAgreementDocuments;
        private List<String> languages;
        private String sourceName;
        private String sourceId;
        private String sourceAccountId;
        private Metadata metadata;

        @Data
        public static class Party {
            private String id;
            private String nameInAgreement;
        }

        @Data
        public static class Provisions {
            private String effectiveDate;
            private String expirationDate;
            private String executionDate;
            private Double totalAgreementValue;
            private String totalAgreementValueCurrencyCode;
            private Double annualAgreementValue;
            private String annualAgreementValueCurrencyCode;
            private String termLength;
            private String assignmentType;
            private String assignmentChangeOfControl;
            private String assignmentTerminationRights;
            private String governingLaw;
            private String jurisdiction;
            private String paymentTermsDueDate;
            private Boolean canChargeLatePaymentFees;
            private Integer latePaymentFeePercent;
            private Integer priceCapPercentIncrease;
            private Double liabilityCapFixedAmount;
            private String liabilityCapCurrencyCode;
            private Integer liabilityCapMultiplier;
            private String liabilityCapDuration;
            private String renewalType;
            private String renewalNoticePeriod;
            private String renewalNoticeDate;
            private String autoRenewalTermLength;
            private String renewalExtensionPeriod;
            private String renewalProcessOwner;
            private String renewalAdditionalInfo;
            private String terminationPeriodForCause;
            private String terminationPeriodForConvenience;
        }

        @Data
        public static class RelatedAgreementDocuments {
            private String parentAgreementDocumentId;
        }

        @Data
        public static class Metadata {
            private String createdAt;
            private String createdBy;
            private String requestId;
            private String responseTimestamp;
            private Integer responseDurationMs;
        }
}
