package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class NavigatorAgreement {
    private String id;
    private String title;
    private String type;
    private String status;
    private String summary;

    private List<LegalProvision> legalProvisions;
    private List<FinancialProvision> financialProvisions;
    private List<LifeCycleProvision> lifecycleProvisions;
    private List<CustomProvision> customProvisions;


    private List<Party> parties;


    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<RelatedAgreement> relatedAgreements;
    private Map<String, Object> customAttributes;

    public NavigatorAgreement() {
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public String getSummary() {
        return this.summary;
    }

    public List<LegalProvision> getLegalProvisions() {
        return this.legalProvisions;
    }

    public List<FinancialProvision> getFinancialProvisions() {
        return this.financialProvisions;
    }

    public List<LifeCycleProvision> getLifecycleProvisions() {
        return this.lifecycleProvisions;
    }

    public List<CustomProvision> getCustomProvisions() {
        return this.customProvisions;
    }

    public List<Party> getParties() {
        return this.parties;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public List<RelatedAgreement> getRelatedAgreements() {
        return this.relatedAgreements;
    }

    public Map<String, Object> getCustomAttributes() {
        return this.customAttributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setLegalProvisions(List<LegalProvision> legalProvisions) {
        this.legalProvisions = legalProvisions;
    }

    public void setFinancialProvisions(List<FinancialProvision> financialProvisions) {
        this.financialProvisions = financialProvisions;
    }

    public void setLifecycleProvisions(List<LifeCycleProvision> lifecycleProvisions) {
        this.lifecycleProvisions = lifecycleProvisions;
    }

    public void setCustomProvisions(List<CustomProvision> customProvisions) {
        this.customProvisions = customProvisions;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setRelatedAgreements(List<RelatedAgreement> relatedAgreements) {
        this.relatedAgreements = relatedAgreements;
    }

    public void setCustomAttributes(Map<String, Object> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NavigatorAgreement)) return false;
        final NavigatorAgreement other = (NavigatorAgreement) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$summary = this.getSummary();
        final Object other$summary = other.getSummary();
        if (this$summary == null ? other$summary != null : !this$summary.equals(other$summary)) return false;
        final Object this$legalProvisions = this.getLegalProvisions();
        final Object other$legalProvisions = other.getLegalProvisions();
        if (this$legalProvisions == null ? other$legalProvisions != null : !this$legalProvisions.equals(other$legalProvisions))
            return false;
        final Object this$financialProvisions = this.getFinancialProvisions();
        final Object other$financialProvisions = other.getFinancialProvisions();
        if (this$financialProvisions == null ? other$financialProvisions != null : !this$financialProvisions.equals(other$financialProvisions))
            return false;
        final Object this$lifecycleProvisions = this.getLifecycleProvisions();
        final Object other$lifecycleProvisions = other.getLifecycleProvisions();
        if (this$lifecycleProvisions == null ? other$lifecycleProvisions != null : !this$lifecycleProvisions.equals(other$lifecycleProvisions))
            return false;
        final Object this$customProvisions = this.getCustomProvisions();
        final Object other$customProvisions = other.getCustomProvisions();
        if (this$customProvisions == null ? other$customProvisions != null : !this$customProvisions.equals(other$customProvisions))
            return false;
        final Object this$parties = this.getParties();
        final Object other$parties = other.getParties();
        if (this$parties == null ? other$parties != null : !this$parties.equals(other$parties)) return false;
        final Object this$createdDate = this.getCreatedDate();
        final Object other$createdDate = other.getCreatedDate();
        if (this$createdDate == null ? other$createdDate != null : !this$createdDate.equals(other$createdDate))
            return false;
        final Object this$modifiedDate = this.getModifiedDate();
        final Object other$modifiedDate = other.getModifiedDate();
        if (this$modifiedDate == null ? other$modifiedDate != null : !this$modifiedDate.equals(other$modifiedDate))
            return false;
        final Object this$relatedAgreements = this.getRelatedAgreements();
        final Object other$relatedAgreements = other.getRelatedAgreements();
        if (this$relatedAgreements == null ? other$relatedAgreements != null : !this$relatedAgreements.equals(other$relatedAgreements))
            return false;
        final Object this$customAttributes = this.getCustomAttributes();
        final Object other$customAttributes = other.getCustomAttributes();
        if (this$customAttributes == null ? other$customAttributes != null : !this$customAttributes.equals(other$customAttributes))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NavigatorAgreement;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $summary = this.getSummary();
        result = result * PRIME + ($summary == null ? 43 : $summary.hashCode());
        final Object $legalProvisions = this.getLegalProvisions();
        result = result * PRIME + ($legalProvisions == null ? 43 : $legalProvisions.hashCode());
        final Object $financialProvisions = this.getFinancialProvisions();
        result = result * PRIME + ($financialProvisions == null ? 43 : $financialProvisions.hashCode());
        final Object $lifecycleProvisions = this.getLifecycleProvisions();
        result = result * PRIME + ($lifecycleProvisions == null ? 43 : $lifecycleProvisions.hashCode());
        final Object $customProvisions = this.getCustomProvisions();
        result = result * PRIME + ($customProvisions == null ? 43 : $customProvisions.hashCode());
        final Object $parties = this.getParties();
        result = result * PRIME + ($parties == null ? 43 : $parties.hashCode());
        final Object $createdDate = this.getCreatedDate();
        result = result * PRIME + ($createdDate == null ? 43 : $createdDate.hashCode());
        final Object $modifiedDate = this.getModifiedDate();
        result = result * PRIME + ($modifiedDate == null ? 43 : $modifiedDate.hashCode());
        final Object $relatedAgreements = this.getRelatedAgreements();
        result = result * PRIME + ($relatedAgreements == null ? 43 : $relatedAgreements.hashCode());
        final Object $customAttributes = this.getCustomAttributes();
        result = result * PRIME + ($customAttributes == null ? 43 : $customAttributes.hashCode());
        return result;
    }

    public String toString() {
        return "NavigatorAgreement(id=" + this.getId() + ", title=" + this.getTitle() + ", type=" + this.getType() + ", status=" + this.getStatus() + ", summary=" + this.getSummary() + ", legalProvisions=" + this.getLegalProvisions() + ", financialProvisions=" + this.getFinancialProvisions() + ", lifecycleProvisions=" + this.getLifecycleProvisions() + ", customProvisions=" + this.getCustomProvisions() + ", parties=" + this.getParties() + ", createdDate=" + this.getCreatedDate() + ", modifiedDate=" + this.getModifiedDate() + ", relatedAgreements=" + this.getRelatedAgreements() + ", customAttributes=" + this.getCustomAttributes() + ")";
    }
}