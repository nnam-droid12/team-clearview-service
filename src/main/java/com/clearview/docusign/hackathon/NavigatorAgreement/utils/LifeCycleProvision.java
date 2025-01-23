package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import java.time.LocalDateTime;
import java.util.Map;

public class LifeCycleProvision {
    private String id;
    private String type;
    private LocalDateTime effectiveDate;
    private LocalDateTime expirationDate;
    private Map<String, Object> attributes;

    public LifeCycleProvision() {
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public LocalDateTime getEffectiveDate() {
        return this.effectiveDate;
    }

    public LocalDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LifeCycleProvision)) return false;
        final LifeCycleProvision other = (LifeCycleProvision) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$effectiveDate = this.getEffectiveDate();
        final Object other$effectiveDate = other.getEffectiveDate();
        if (this$effectiveDate == null ? other$effectiveDate != null : !this$effectiveDate.equals(other$effectiveDate))
            return false;
        final Object this$expirationDate = this.getExpirationDate();
        final Object other$expirationDate = other.getExpirationDate();
        if (this$expirationDate == null ? other$expirationDate != null : !this$expirationDate.equals(other$expirationDate))
            return false;
        final Object this$attributes = this.getAttributes();
        final Object other$attributes = other.getAttributes();
        if (this$attributes == null ? other$attributes != null : !this$attributes.equals(other$attributes))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LifeCycleProvision;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $effectiveDate = this.getEffectiveDate();
        result = result * PRIME + ($effectiveDate == null ? 43 : $effectiveDate.hashCode());
        final Object $expirationDate = this.getExpirationDate();
        result = result * PRIME + ($expirationDate == null ? 43 : $expirationDate.hashCode());
        final Object $attributes = this.getAttributes();
        result = result * PRIME + ($attributes == null ? 43 : $attributes.hashCode());
        return result;
    }

    public String toString() {
        return "LifeCycleProvision(id=" + this.getId() + ", type=" + this.getType() + ", effectiveDate=" + this.getEffectiveDate() + ", expirationDate=" + this.getExpirationDate() + ", attributes=" + this.getAttributes() + ")";
    }
}
