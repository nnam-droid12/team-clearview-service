package com.clearview.docusign.hackathon.Milestone.entities;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "milestone")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneId;

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    @JsonBackReference
    private Agreement agreement;

    public Milestone(Long milestoneId, String title, String description, LocalDateTime dueDate, String status, Agreement agreement) {
        this.milestoneId = milestoneId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.agreement = agreement;
    }

    public Milestone() {
    }

    public static MilestoneBuilder builder() {
        return new MilestoneBuilder();
    }

    public Long getMilestoneId() {
        return this.milestoneId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getDueDate() {
        return this.dueDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Agreement getAgreement() {
        return this.agreement;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Milestone)) return false;
        final Milestone other = (Milestone) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$milestoneId = this.getMilestoneId();
        final Object other$milestoneId = other.getMilestoneId();
        if (this$milestoneId == null ? other$milestoneId != null : !this$milestoneId.equals(other$milestoneId))
            return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$dueDate = this.getDueDate();
        final Object other$dueDate = other.getDueDate();
        if (this$dueDate == null ? other$dueDate != null : !this$dueDate.equals(other$dueDate)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$agreement = this.getAgreement();
        final Object other$agreement = other.getAgreement();
        if (this$agreement == null ? other$agreement != null : !this$agreement.equals(other$agreement)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Milestone;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $milestoneId = this.getMilestoneId();
        result = result * PRIME + ($milestoneId == null ? 43 : $milestoneId.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $dueDate = this.getDueDate();
        result = result * PRIME + ($dueDate == null ? 43 : $dueDate.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $agreement = this.getAgreement();
        result = result * PRIME + ($agreement == null ? 43 : $agreement.hashCode());
        return result;
    }

    public String toString() {
        return "Milestone(milestoneId=" + this.getMilestoneId() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", dueDate=" + this.getDueDate() + ", status=" + this.getStatus() + ", agreement=" + this.getAgreement() + ")";
    }

    public static class MilestoneBuilder {
        private Long milestoneId;
        private String title;
        private String description;
        private LocalDateTime dueDate;
        private String status;
        private Agreement agreement;

        MilestoneBuilder() {
        }

        public MilestoneBuilder milestoneId(Long milestoneId) {
            this.milestoneId = milestoneId;
            return this;
        }

        public MilestoneBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MilestoneBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MilestoneBuilder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public MilestoneBuilder status(String status) {
            this.status = status;
            return this;
        }

        public MilestoneBuilder agreement(Agreement agreement) {
            this.agreement = agreement;
            return this;
        }

        public Milestone build() {
            return new Milestone(this.milestoneId, this.title, this.description, this.dueDate, this.status, this.agreement);
        }

        public String toString() {
            return "Milestone.MilestoneBuilder(milestoneId=" + this.milestoneId + ", title=" + this.title + ", description=" + this.description + ", dueDate=" + this.dueDate + ", status=" + this.status + ", agreement=" + this.agreement + ")";
        }
    }
}