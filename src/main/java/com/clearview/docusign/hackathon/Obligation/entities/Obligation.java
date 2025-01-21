package com.clearview.docusign.hackathon.Obligation.entities;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "obligation")
public class Obligation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obligationId;

    private String description;

    private LocalDateTime dueDate;

    private String assignedTo;

    private String status;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    public Obligation(Long obligationId, String description, LocalDateTime dueDate, String assignedTo, String status, Agreement agreement) {
        this.obligationId = obligationId;
        this.description = description;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
        this.status = status;
        this.agreement = agreement;
    }

    public Obligation() {
    }

    public static ObligationBuilder builder() {
        return new ObligationBuilder();
    }

    public Long getObligationId() {
        return this.obligationId;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getDueDate() {
        return this.dueDate;
    }

    public String getAssignedTo() {
        return this.assignedTo;
    }

    public String getStatus() {
        return this.status;
    }

    public Agreement getAgreement() {
        return this.agreement;
    }

    public void setObligationId(Long obligationId) {
        this.obligationId = obligationId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Obligation)) return false;
        final Obligation other = (Obligation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$obligationId = this.getObligationId();
        final Object other$obligationId = other.getObligationId();
        if (this$obligationId == null ? other$obligationId != null : !this$obligationId.equals(other$obligationId))
            return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$dueDate = this.getDueDate();
        final Object other$dueDate = other.getDueDate();
        if (this$dueDate == null ? other$dueDate != null : !this$dueDate.equals(other$dueDate)) return false;
        final Object this$assignedTo = this.getAssignedTo();
        final Object other$assignedTo = other.getAssignedTo();
        if (this$assignedTo == null ? other$assignedTo != null : !this$assignedTo.equals(other$assignedTo))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$agreement = this.getAgreement();
        final Object other$agreement = other.getAgreement();
        if (this$agreement == null ? other$agreement != null : !this$agreement.equals(other$agreement)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Obligation;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $obligationId = this.getObligationId();
        result = result * PRIME + ($obligationId == null ? 43 : $obligationId.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $dueDate = this.getDueDate();
        result = result * PRIME + ($dueDate == null ? 43 : $dueDate.hashCode());
        final Object $assignedTo = this.getAssignedTo();
        result = result * PRIME + ($assignedTo == null ? 43 : $assignedTo.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $agreement = this.getAgreement();
        result = result * PRIME + ($agreement == null ? 43 : $agreement.hashCode());
        return result;
    }

    public String toString() {
        return "Obligation(obligationId=" + this.getObligationId() + ", description=" + this.getDescription() + ", dueDate=" + this.getDueDate() + ", assignedTo=" + this.getAssignedTo() + ", status=" + this.getStatus() + ", agreement=" + this.getAgreement() + ")";
    }

    public static class ObligationBuilder {
        private Long obligationId;
        private String description;
        private LocalDateTime dueDate;
        private String assignedTo;
        private String status;
        private Agreement agreement;

        ObligationBuilder() {
        }

        public ObligationBuilder obligationId(Long obligationId) {
            this.obligationId = obligationId;
            return this;
        }

        public ObligationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ObligationBuilder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public ObligationBuilder assignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        public ObligationBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ObligationBuilder agreement(Agreement agreement) {
            this.agreement = agreement;
            return this;
        }

        public Obligation build() {
            return new Obligation(this.obligationId, this.description, this.dueDate, this.assignedTo, this.status, this.agreement);
        }

        public String toString() {
            return "Obligation.ObligationBuilder(obligationId=" + this.obligationId + ", description=" + this.description + ", dueDate=" + this.dueDate + ", assignedTo=" + this.assignedTo + ", status=" + this.status + ", agreement=" + this.agreement + ")";
        }
    }
}
