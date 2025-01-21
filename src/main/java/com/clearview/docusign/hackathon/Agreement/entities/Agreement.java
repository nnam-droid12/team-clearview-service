package com.clearview.docusign.hackathon.Agreement.entities;

import com.clearview.docusign.hackathon.Milestone.entities.Milestone;
import com.clearview.docusign.hackathon.Obligation.entities.Obligation;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agreement")
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agreementId;

    private String docuSignEnvelopeId;

    private String status;

    private LocalDateTime signedDate;

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL)
    private List<Milestone> milestones = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL)
    private List<Obligation> obligations = new ArrayList<>();

    public Agreement(Long agreementId, String docuSignEnvelopeId, String status, LocalDateTime signedDate, List<Milestone> milestones, List<Obligation> obligations) {
        this.agreementId = agreementId;
        this.docuSignEnvelopeId = docuSignEnvelopeId;
        this.status = status;
        this.signedDate = signedDate;
        this.milestones = milestones;
        this.obligations = obligations;
    }

    public Agreement() {
    }

    public static AgreementBuilder builder() {
        return new AgreementBuilder();
    }

    public Long getAgreementId() {
        return this.agreementId;
    }

    public String getDocuSignEnvelopeId() {
        return this.docuSignEnvelopeId;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDateTime getSignedDate() {
        return this.signedDate;
    }

    public List<Milestone> getMilestones() {
        return this.milestones;
    }

    public List<Obligation> getObligations() {
        return this.obligations;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public void setDocuSignEnvelopeId(String docuSignEnvelopeId) {
        this.docuSignEnvelopeId = docuSignEnvelopeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSignedDate(LocalDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }

    public void setObligations(List<Obligation> obligations) {
        this.obligations = obligations;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Agreement)) return false;
        final Agreement other = (Agreement) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$agreementId = this.getAgreementId();
        final Object other$agreementId = other.getAgreementId();
        if (this$agreementId == null ? other$agreementId != null : !this$agreementId.equals(other$agreementId))
            return false;
        final Object this$docuSignEnvelopeId = this.getDocuSignEnvelopeId();
        final Object other$docuSignEnvelopeId = other.getDocuSignEnvelopeId();
        if (this$docuSignEnvelopeId == null ? other$docuSignEnvelopeId != null : !this$docuSignEnvelopeId.equals(other$docuSignEnvelopeId))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$signedDate = this.getSignedDate();
        final Object other$signedDate = other.getSignedDate();
        if (this$signedDate == null ? other$signedDate != null : !this$signedDate.equals(other$signedDate))
            return false;
        final Object this$milestones = this.getMilestones();
        final Object other$milestones = other.getMilestones();
        if (this$milestones == null ? other$milestones != null : !this$milestones.equals(other$milestones))
            return false;
        final Object this$obligations = this.getObligations();
        final Object other$obligations = other.getObligations();
        if (this$obligations == null ? other$obligations != null : !this$obligations.equals(other$obligations))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Agreement;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $agreementId = this.getAgreementId();
        result = result * PRIME + ($agreementId == null ? 43 : $agreementId.hashCode());
        final Object $docuSignEnvelopeId = this.getDocuSignEnvelopeId();
        result = result * PRIME + ($docuSignEnvelopeId == null ? 43 : $docuSignEnvelopeId.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $signedDate = this.getSignedDate();
        result = result * PRIME + ($signedDate == null ? 43 : $signedDate.hashCode());
        final Object $milestones = this.getMilestones();
        result = result * PRIME + ($milestones == null ? 43 : $milestones.hashCode());
        final Object $obligations = this.getObligations();
        result = result * PRIME + ($obligations == null ? 43 : $obligations.hashCode());
        return result;
    }

    public String toString() {
        return "Agreement(agreementId=" + this.getAgreementId() + ", docuSignEnvelopeId=" + this.getDocuSignEnvelopeId() + ", status=" + this.getStatus() + ", signedDate=" + this.getSignedDate() + ", milestones=" + this.getMilestones() + ", obligations=" + this.getObligations() + ")";
    }

    public static class AgreementBuilder {
        private Long agreementId;
        private String docuSignEnvelopeId;
        private String status;
        private LocalDateTime signedDate;
        private List<Milestone> milestones;
        private List<Obligation> obligations;

        AgreementBuilder() {
        }

        public AgreementBuilder agreementId(Long agreementId) {
            this.agreementId = agreementId;
            return this;
        }

        public AgreementBuilder docuSignEnvelopeId(String docuSignEnvelopeId) {
            this.docuSignEnvelopeId = docuSignEnvelopeId;
            return this;
        }

        public AgreementBuilder status(String status) {
            this.status = status;
            return this;
        }

        public AgreementBuilder signedDate(LocalDateTime signedDate) {
            this.signedDate = signedDate;
            return this;
        }

        public AgreementBuilder milestones(List<Milestone> milestones) {
            this.milestones = milestones;
            return this;
        }

        public AgreementBuilder obligations(List<Obligation> obligations) {
            this.obligations = obligations;
            return this;
        }

        public Agreement build() {
            return new Agreement(this.agreementId, this.docuSignEnvelopeId, this.status, this.signedDate, this.milestones, this.obligations);
        }

        public String toString() {
            return "Agreement.AgreementBuilder(agreementId=" + this.agreementId + ", docuSignEnvelopeId=" + this.docuSignEnvelopeId + ", status=" + this.status + ", signedDate=" + this.signedDate + ", milestones=" + this.milestones + ", obligations=" + this.obligations + ")";
        }
    }
}
