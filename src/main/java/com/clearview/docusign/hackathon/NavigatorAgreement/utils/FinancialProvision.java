package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import java.math.BigDecimal;
import java.util.Map;

class FinancialProvision {
    private String id;
    private String type;
    private BigDecimal amount;
    private String currency;
    private Map<String, Object> attributes;

    public FinancialProvision() {
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getCurrency() {
        return this.currency;
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

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FinancialProvision)) return false;
        final FinancialProvision other = (FinancialProvision) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$amount = this.getAmount();
        final Object other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !this$amount.equals(other$amount)) return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
        final Object this$attributes = this.getAttributes();
        final Object other$attributes = other.getAttributes();
        if (this$attributes == null ? other$attributes != null : !this$attributes.equals(other$attributes))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FinancialProvision;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $amount = this.getAmount();
        result = result * PRIME + ($amount == null ? 43 : $amount.hashCode());
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $attributes = this.getAttributes();
        result = result * PRIME + ($attributes == null ? 43 : $attributes.hashCode());
        return result;
    }

    public String toString() {
        return "FinancialProvision(id=" + this.getId() + ", type=" + this.getType() + ", amount=" + this.getAmount() + ", currency=" + this.getCurrency() + ", attributes=" + this.getAttributes() + ")";
    }
}
